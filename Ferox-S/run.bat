@echo off
title Run/Build
echo Building...
"C:\Program Files\Zulu\zulu-15/bin/javac" -cp lib/*; -d bin -sourcepath src src/*.java
echo Build has successfully completed. 
echo Starting [YOUR SERVER TITLE] ...
"C:\Program Files\Zulu\zulu-15/bin/java.exe" -server -Xmx1024m -cp bin;lib/*; com.ferox.GameServer
pause