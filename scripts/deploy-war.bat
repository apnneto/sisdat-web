@echo off
REM Deploy WAR to GlassFish using asadmin
SET GLASSFISH_HOME=C:\glassfish3
SET WAR_PATH=%1
IF "%WAR_PATH%"=="" (
  echo Usage: deploy-war.bat C:\path\to\yourapp.war
  exit /b 1
)
"%GLASSFISH_HOME%\bin\asadmin.bat" deploy --force "%WAR_PATH%"
echo Deploy command sent.