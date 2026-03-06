@echo off
REM ---------------------------------------------------------------
REM setenv.bat — configure JAVA_HOME to JDK 21 (Eclipse Temurin)
REM Run this once per cmd session if JAVA_HOME is still pointing
REM at JDK 8, or open a new terminal after running setx above.
REM ---------------------------------------------------------------
set "JAVA_HOME=C:\eclipse-jee-2025-12-R-win32-x86_64\eclipse\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.9.v20251105-0741\jre"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME set to %JAVA_HOME%
java -version
mvn -version
