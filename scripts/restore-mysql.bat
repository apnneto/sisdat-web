@echo off
REM restore-mysql.bat
REM Uso: restore-mysql.bat <mysql_exe_or_path> <db_name> <db_user> <db_password> <sql_file>
REM Exemplo: restore-mysql.bat mysql sisdat_schema root "senha" src\META-INF\script.sql

SETLOCAL ENABLEDELAYEDEXPANSION
nSET MYSQL_EXE=%~1nSET DB_NAME=%~2nSET DB_USER=%~3nSET DB_PASS=%~4nSET SQL_FILE=%~5
nIF "%MYSQL_EXE%"=="" (
  echo Uso: %~nx0 ^<mysql_exe_or_path^> ^<db_name^> ^<db_user^> ^<db_password^> ^<sql_file^>
  echo Ex: %~nx0 mysql sisdat root "senha" src\META-INF\script.sql
  exit /b 1
)
IF "%DB_NAME%"=="" (
  echo DB_name nao fornecido
  exit /b 1
)
IF "%DB_USER%"=="" (
  echo DB user nao fornecido
  exit /b 1
)
IF "%SQL_FILE%"=="" (
  echo SQL file nao fornecido
  exit /b 1
)
nREM Resolve caminho do executavel mysql: se for apenas 'mysql' esperamos que esteja no PATH
where %MYSQL_EXE% >nul 2>&1
IF ERRORLEVEL 1 (
  REM tentar usar o caminho literal passado
  IF EXIST "%MYSQL_EXE%" (
    SET MYSQL_CMD="%MYSQL_EXE%"
  ) ELSE (
    echo Nao foi possivel localizar o executavel mysql: %MYSQL_EXE%
    echo Coloque o cliente mysql no PATH ou informe o caminho completo para o executavel.
    exit /b 1
  )
) ELSE (
  SET MYSQL_CMD=%MYSQL_EXE%
)
nIF NOT EXIST "%SQL_FILE%" (
  echo Arquivo SQL nao encontrado: %SQL_FILE%
  exit /b 1
)
necho Criando banco (se nao existir) e restaurando: %DB_NAME% from %SQL_FILE%
nREM Criar banco se nao existir
IF "%DB_PASS%"=="" (
  %MYSQL_CMD% -u%DB_USER% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" || (echo Falha ao criar DB & exit /b 1)
  %MYSQL_CMD% -u%DB_USER% %DB_NAME% < "%SQL_FILE%" || (echo Falha ao importar SQL & exit /b 1)
) ELSE (
  %MYSQL_CMD% -u%DB_USER% -p%DB_PASS% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" || (echo Falha ao criar DB & exit /b 1)
  %MYSQL_CMD% -u%DB_USER% -p%DB_PASS% %DB_NAME% < "%SQL_FILE%" || (echo Falha ao importar SQL & exit /b 1)
)
necho Importacao concluida com sucesso.
ENDLOCAL
