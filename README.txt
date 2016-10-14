How to compile :
- open this project in eclipse (File -> Import -> Existing projects)
- Add JAR of urbi from directory /usr/local/gostai/share/sdk-remote/java/lib
- build

How to launch :
- eclipse : 
  * open "ReetiControl" properties, then choose "Run/Debug Settings" -> "Edit" -> "Arguments"
    If no configuration is available in "Run/Debug Settings", click "New.."-> "Java Application" -> "OK", search Main.java for "Main class:", click "OK", then "Edit" -> "Arguments".
  * in the text field "VM arguments", add : "-Djava.library.path=/usr/local/gostai/lib/"
  * press the launch button
  
Network configuration settings :
-  ReetiControl side:
   Line 13 in Main.java
   ReceiveCommand recmd = new ReceiveCommand("134.96.240.30", 1255,"134.96.240.31", 1423);
   The first two paramaters are the IP address and port of Reeti.
   The second two paramaters are the IP address and port of the computer on which ReetiV2Engine is launched.
-  ReetiV2Engine side:
   Line 97 in MotorWrapper.java in package de.hcm.robots.reetiV2
   mMessageTransmission = new MessageTransmission("134.96.240.32", 1423, "134.96.240.30", 1255);
   The first two paramaters are the IP address and port of the computer on which ReetiV2Engine is launched.
   The second two paramaters are the IP address and port of Reeti.
-  The ports must be different from the ports used by UDP between ReetiV2Engine and VisualSceneMaker. 
