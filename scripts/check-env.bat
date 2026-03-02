@echo off
REM check-env.bat - verifica variáveis de ambiente e ferramentas necessárias para o projeto SisDAT

echo === Verificando variaveis de ambiente e ferramentas para SisDAT ===
echo.
echo JAVA_HOME:
echo %JAVA_HOME%
echo.
echo java -version:
where java >nul 2>&1
IF ERRORLEVEL 1 (
  echo java nao encontrado
) ELSE (
  java -version
)
echo.
echo Maven (mvn) e M2_HOME:
echo M2_HOME=%M2_HOME% MAVEN_HOME=%MAVEN_HOME%
where mvn >nul 2>&1
IF ERRORLEVEL 1 (
  echo mvn nao encontrado no PATH
) ELSE (
  mvn -v
)
echo.
echo Docker:
where docker >nul 2>&1
IF ERRORLEVEL 1 (
  echo Docker nao encontrado
) ELSE (
  docker --version
)
rem docker compose may be subcommand
where docker >nul 2>&1
IF ERRORLEVEL 1 (
  rem nothing
) ELSE (
  docker compose version 2>nul || docker-compose --version 2>nul || echo Docker Compose nao encontrado
)
echo.
echo MySQL client:
where mysql >nul 2>&1
IF ERRORLEVEL 1 (
  echo mysql cliente nao encontrado no PATH - necessario para scripts\restore-mysql.bat
) ELSE (
  mysql --version
)
echo.
echo Verificando conectividade TCP para MySQL em localhost:3306 (Test-NetConnection via PowerShell):
powershell -Command "try { $r=Test-NetConnection -ComputerName 'localhost' -Port 3306 -WarningAction SilentlyContinue; if($r.TcpTestSucceeded){ Write-Host 'TCP 3306 aberto em localhost (MySQL possivelmente acessivel)'; } else { Write-Host 'TCP 3306 fechado ou inacessivel em localhost'; }; exit 0 } catch { Write-Host 'Falha ao executar Test-NetConnection (PowerShell pode nao estar disponivel)'; exit 0 }"
echo.
rem Se o cliente mysql existir, tentar verificar existencia do schema sisdat_schema (sem senha/usuario — se necessario passe parametros manualmente)
where mysql >nul 2>&1
IF ERRORLEVEL 1 (
  echo Cliente mysql indisponivel, porem voce informou que o DB esta em localhost; para checar schema execute localmente: "mysql -u <user> -p -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='sisdat_schema';""
) ELSE (
  echo Tentando verificar existencia do schema 'sisdat_schema' usando cliente mysql (sem senha)...
  mysql -N -s -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='sisdat_schema';" 2>nul > temp_db_check.txt || (echo Falha ao executar mysql sem credenciais; tente: mysql -u <user> -p -e "SELECT ..." & del temp_db_check.txt & goto :DB_CHECK_DONE)
  set /p DB_EXISTS= < temp_db_check.txt
  if "%DB_EXISTS%"=="sisdat_schema" (
    echo Schema 'sisdat_schema' encontrado no servidor MySQL (conectado com as credenciais atuais).
  ) ELSE (
    echo Schema 'sisdat_schema' NAO encontrado ou necessaria autenticacao. Se o banco estiver em localhost, execute manualmente: mysql -u <user> -p -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='sisdat_schema';"
  )
  del temp_db_check.txt >nul 2>&1
)
echo.
echo GLASSFISH_HOME (scripts usam C:\glassfish3 por padrao):
IF "%GLASSFISH_HOME%"=="" (
  goto GF_NOT_DEFINED
)
echo %GLASSFISH_HOME%
IF EXIST "%GLASSFISH_HOME%\bin\asadmin.bat" (
  echo asadmin encontrado em %GLASSFISH_HOME%\bin\asadmin.bat
) ELSE (
  echo asadmin nao encontrado em %GLASSFISH_HOME%\bin\asadmin.bat
)
goto GF_CHECK_DONE
:GF_NOT_DEFINED
echo GLASSFISH_HOME nao definido (scripts usam C:\glassfish3 por padrao). Se usar GlassFish local, defina a variavel ou edite os scripts.
:GF_CHECK_DONE
echo.
echo Checando arquivos do projeto:
IF EXIST "%~dp0..\WebContent\WEB-INF\lib\mysql-connector-java-*.jar" (
  echo Driver MySQL encontrado em WebContent\WEB-INF\lib
) ELSE (
  echo Driver MySQL NAO encontrado em WebContent\WEB-INF\lib - necessario para deploy local em GlassFish (ou use container)
)
IF EXIST "%~dp0..\META-INF\script.sql" (
  echo META-INF\script.sql encontrado (usado por docker-compose)
) ELSE (
  echo META-INF\script.sql NAO encontrado - verifique caminho (o SQL atual esta em src\META-INF\script.sql)
)
echo.
echo === FIM da verificacao ===
pause