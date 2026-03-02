@echo off
REM configure-glassfish-datasource.bat
REM Uso: configure-glassfish-datasource.bat <GLASSFISH_HOME> <db_name> <db_user> <db_pass> <pool_name> <jndi_name>
REM Ex: configure-glassfish-datasource.bat C:\glassfish3 sisdat root "senha" sisdatPool jdbc/sisdat

SETLOCAL ENABLEDELAYEDEXPANSION

SET GLASSFISH_HOME=%~1
SET DB_NAME=%~2
SET DB_USER=%~3
SET DB_PASS=%~4
SET POOL_NAME=%~5
SET JNDI_NAME=%~6

IF "%GLASSFISH_HOME%"=="" (
  echo Uso: %~nx0 ^<GLASSFISH_HOME^> ^<db_name^> ^<db_user^> ^<db_pass^> ^<pool_name^> ^<jndi_name^>
  exit /b 1
)
IF NOT EXIST "%GLASSFISH_HOME%\bin\asadmin.bat" (
  echo GlassFish asadmin nao encontrado em %GLASSFISH_HOME%\bin\asadmin.bat
  exit /b 1
)
IF "%POOL_NAME%"=="" (
  echo Pool name nao fornecido
  exit /b 1
)
IF "%JNDI_NAME%"=="" (
  echo JNDI name nao fornecido
  exit /b 1
)
nREM Procura por driver MySQL no projeto
SET DRIVER_JAR=
FOR %%F IN ("WebContent\WEB-INF\lib\mysql-connector-java-*.jar") DO (
  SET DRIVER_JAR=%%F
)
IF "%DRIVER_JAR%"=="" (
  echo Nao encontrei mysql-connector-java no projeto em WebContent\\WEB-INF\\lib. Procure adicionar o driver manualmente em %GLASSFISH_HOME%\\glassfish\\domains\\domain1\\lib
) ELSE (
  echo Copiando driver %DRIVER_JAR% para %GLASSFISH_HOME%\glassfish\domains\domain1\lib\
  copy "%DRIVER_JAR%" "%GLASSFISH_HOME%\glassfish\domains\domain1\lib\" >nul
)
nREM Monta a URL JDBC para MySQL local (host=localhost, port=3306)
SET JDBC_URL=jdbc:mysql://localhost:3306/%DB_NAME%?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
nREM Cria connection pool
"%GLASSFISH_HOME%\bin\asadmin.bat" create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property user=%DB_USER%;password=%DB_PASS%;URL=%JDBC_URL% %POOL_NAME%
IF ERRORLEVEL 1 (
  echo Falha ao criar connection-pool %POOL_NAME% - pode ja existir. Tentando ping...
)
nREM Ping connection pool
"%GLASSFISH_HOME%\bin\asadmin.bat" ping-connection-pool %POOL_NAME%
IF ERRORLEVEL 1 (
  echo Ping do connection pool falhou. Verifique se o driver foi copiado e se o banco esta acessivel.
  exit /b 1
) ELSE (
  echo Connection pool OK.
)
nREM Cria JDBC resource (JNDI)
"%GLASSFISH_HOME%\bin\asadmin.bat" create-jdbc-resource --connectionpoolid %POOL_NAME% %JNDI_NAME%
IF ERRORLEVEL 1 (
  echo Falha ao criar JDBC resource %JNDI_NAME% - verifique se ja existe.
  exit /b 1
) ELSE (
  echo JDBC resource criado com sucesso: %JNDI_NAME%
)
nENDLOCAL
