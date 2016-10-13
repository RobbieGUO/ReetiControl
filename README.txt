How to compile :
- open this project in eclipse (File -> Import -> Existing projects)
- Add JAR of urbi from directory /usr/local/gostai/share/sdk-remote/java/lib
- build

How to launch :
- eclipse : 
  * open "UrbiCOntrol" properties, then choose "Run/Debug Settings" -> "Edit" -> "Arguments"
  * in the text field "VM arguments", add : "-Djava.library.path=/usr/local/gostai/lib/"
  * press the launch button