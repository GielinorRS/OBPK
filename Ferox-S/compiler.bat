@echo off
echo Compiling...
"C:\Program Files\Java\jdk-14.0.2\bin\javac.exe" -d bin -cp lib/*; -sourcepath src/com/ferox/*.java
@echo Finished.
pause