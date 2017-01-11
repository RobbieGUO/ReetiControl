package cereprocTts;

import Main.Main;

public class LipMove extends Thread {

//	private Main reeticlient;
	private Boolean runflag;

	public LipMove() {
//		this.reeticlient = reeticlient;
		runflag = true;
	}

	public void setrunflag(Boolean b) {
		runflag = b;
	}

	@Override
	public void run() {
		while (runflag) {
			System.out.println("Lip Move");
			
			Main.client.send("Global.servo.bottomLip = 40.0 smooth:0.3s;");
			try {
				sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Main.client.send("Global.servo.bottomLip = 75.0 smooth:0.3s;");
			try {
				sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
