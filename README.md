# NatPoacher

javac -h src/main/resources/jni src/main/java/vinneg/natpoacher/MouseCursor.java

g++ -I"C:\Program Files\Java\jdk-25\include" -I"C:\Program Files\Java\jdk-25\include\win32" -shared -lgdi32 -o src/main/resources/jni/MouseCursorLib.dll src/main/resources/jni/vinneg_natpoacher_MouseCursor.cpp -l Gdi32