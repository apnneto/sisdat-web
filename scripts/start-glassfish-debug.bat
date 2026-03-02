@echo off
REM Start GlassFish domain1 in debug mode (Windows)
SET GLASSFISH_HOME=C:\glassfish3
IF NOT EXIST "%GLASSFISH_HOME%\bin\asadmin.bat" (
  echo GlassFish not found in %GLASSFISH_HOME%. Please edit this script and set GLASSFISH_HOME.
  exit /b 1
)
echo Starting GlassFish domain1 in debug mode...
"%GLASSFISH_HOME%\bin\asadmin.bat" start-domain --debug
echo Done. Use port 9009 (default) for remote debug in Eclipse.
