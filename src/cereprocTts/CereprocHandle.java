package cereprocTts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;

import Main.ReceiveCommand;

import com.cereproc.cerevoice_eng.*;

public class CereprocHandle extends Thread {

	private ReceiveCommand recmd;
	private SWIGTYPE_p_CPRCEN_engine eng;
	private int chan_handle, res;
	private String voice_name;
	private String license_name;
	private String content;
	private String rate_str;
	private TtsEngineCallback cb;
	private Float rate;
	private byte[] utf8bytes;
	private DatagramPacket mDatagramPacket;

	public CereprocHandle(String voice_name, String license_name, String content, ReceiveCommand recmd, DatagramPacket mDatagramPacket) {
		this.voice_name = voice_name;
		this.license_name = license_name;
		this.content = content;
		this.recmd = recmd;
		this.mDatagramPacket = mDatagramPacket;
	}
	
	@Override
	public void run() {
		LipMove lipmove = new LipMove();
		lipmove.start();

		// Load the voice
//		System.out.println("INFO: creating CereVoice Engine");
		eng = cerevoice_eng.CPRCEN_engine_new();
//		System.out.println("INFO: loading voice");
		res = cerevoice_eng.CPRCEN_engine_load_voice(eng, license_name, "",
				voice_name, CPRC_VOICE_LOAD_TYPE.CPRC_VOICE_LOAD);
		if (res == 0) {
			System.out.println("ERROR: unable to load voice file '"
					+ voice_name + "', exiting");
			System.exit(-1);
		}

		// Open the default synthesis channel
//		System.out.println("INFO: opening default channel");
		chan_handle = cerevoice_eng.CPRCEN_engine_open_default_channel(eng);
		if (chan_handle == 0) {
			System.out
					.println("ERROR: unable to open default channel, exiting");
			System.exit(-1);
		}

		// Get some information about the voice used on the channel, here the
		// sample rate
		rate_str = cerevoice_eng.CPRCEN_channel_get_voice_info(eng,
				chan_handle, "SAMPLE_RATE");
//		System.out.println("INFO: channel has a sample rate of: " + rate_str);

		// Open an audio line for output
		rate = new Float(rate_str);
		Audioline au = new Audioline(rate.floatValue());
		// Set our callback class onto it
		cb = new MyTtsCallback(au.line());
		cb.SetCallback(eng, chan_handle);

//		String strLine = " My name, is Anna.";
		try {
			utf8bytes = content.getBytes("UTF-8");
			cerevoice_eng.CPRCEN_engine_channel_speak(eng, chan_handle, content
					+ "\n", utf8bytes.length + 1, 0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Flush engine
		cerevoice_eng.CPRCEN_engine_channel_speak(eng, chan_handle, "", 0, 1);
		// stop lip moving
		lipmove.setrunflag(false);
		// Flush any remaining audio
		au.flush();
		cb.ClearCallback(eng, chan_handle);
		// Close the channel
		cerevoice_eng.CPRCEN_engine_channel_close(eng, chan_handle);
		// Delete the engine
		cerevoice_eng.CPRCEN_engine_delete(eng);
		System.out.println("INFO: finish");
		recmd.sendString(mDatagramPacket,"finish");
	}
}
