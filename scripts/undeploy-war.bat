@echo off
REM Undeploy an app from GlassFish using asadmin
SET GLASSFISH_HOME=C:\glassfish3
SET APP_NAME=%1
IF "%APP_NAME%"=="" (
  echo Usage: undeploy-war.bat appName
  exit /b 1
)
"%GLASSFISH_HOME%\bin\asadmin.bat" undeploy "%APP_NAME%"
echo Undeploy command sent.
