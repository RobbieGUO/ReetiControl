The project is used for:
1, Receive motor commands from VSM, then send corresponding urbi commands to reeti.
2, Receive cerevoice commands from VSM, then generate voice from Reeti side, move Reeti's mouth and send feedback to VSM.

How to compile :
- open this project in eclipse.
- open "ReetiControl" properties, choose "Java Build Path", add liburbijava.jar from directory /usr/local/gostai/share/sdk-remote/java/lib.
- Add cerevoice_eng.jar to "Java Build Path", too. If you do not have cerevoice_eng.jar, please go to  https://www.cereproc.com/de/products/sdk.
- build

How to launch(The project should be launched on Reeti):
- line 20, voice_name is the directory of your voce file (eg 'cerevoice_heather_3.0.9_22k.voice'). 
- line 21 license_name is the directory of the license key of your voce file.
- line30 and line 31 is used to set cerevoice_eng.jar on the library path.
  the string parameter of addDir is the directory of cerevoice_eng.jar.
  the string parameter of System.loadLibrary is  "cerevoice_eng".
- eclipse: 
  * open "ReetiControl" properties, choose "Run/Debug Settings" -> "Edit" -> "Arguments"
    If no configuration is available in "Run/Debug Settings", click "New.."-> "Java Application" -> "OK", search Main.java for "Main class:", click "OK", then "Edit" -> "Arguments".
  * in the text field "VM arguments", add : "-Djava.library.path=/usr/local/gostai/lib/". Click OK.
  * press the launch button.
  
  Port
  - the port used in the project is 1256
 
