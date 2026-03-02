@echo off
REM run-docker-windows.bat — build WAR, run docker-compose (Payara + MySQL), wait for readiness and do a smoke test.
REM Usage: run-docker-windows.bat

setlocal enableextensions enabledelayedexpansion

echo ==================================================
echo SisDAT - Docker run helper
echo ==================================================
:: 1) Ensure we're in project root (script lives in scripts/)
pushd "%~dp0.."
set PROJECT_ROOT=%CD%
echo Project root: %PROJECT_ROOT%
:: 2) Check Maven and build WAR
echo.
echo Building WAR with Maven (skip tests)...
mvn -DskipTests package
if errorlevel 1 (
  echo Maven build failed. Fix errors and retry.
  popd
  exit /b 1
)
:: 3) Detect Docker and Docker Compose
echo.
echo Checking for Docker...
docker version >nul 2>&1if errorlevel 1 (
  echo Docker command not found. Please install Docker Desktop and ensure 'docker' is on PATH.
  popd
  exit /b 1
)
echo Docker found.
:: Prefer docker-compose if available, otherwise use 'docker compose'
echo Checking for docker-compose...
docker-compose version >nul 2>&1if not errorlevel 1 (
  set COMPOSE_CMD=docker-compose
) else (
  docker compose version >nul 2>&1
  if not errorlevel 1 (
    set COMPOSE_CMD=docker compose
  ) else (
    echo Neither 'docker-compose' nor 'docker compose' available. Please install docker-compose or use Docker CLI v2 (docker compose).
    popd
    exit /b 1
  )
)
echo Using compose command: %COMPOSE_CMD%
:: 4) Start the stack (MySQL + Payara) using docker-compose.yml in project root
echo.
echo Starting docker-compose stack (this may take a minute)...
%COMPOSE_CMD% up -d --build
if errorlevel 1 (
  echo docker-compose failed. Check docker output and logs.
  popd
  exit /b 1
)
:: 5) Wait for Payara (HTTP 8080) to respond — test application context /sisdat-web/ (max 180s)
echo.
echo Waiting for application to become responsive on http://localhost:8080/sisdat-web/ ...
set /a WAIT_SECONDS=0
set MAX_WAIT=180
:wait_loop
  rem Use PowerShell's Invoke-WebRequest to test; suppress certificate errors
  powershell -Command "try { $r = Invoke-WebRequest -UseBasicParsing -Uri 'http://localhost:8080/sisdat-web/quiz/buscarUsuarios' -TimeoutSec 10; if ($r.StatusCode -eq 200) { exit 0 } else { exit 2 } } catch { exit 3 }"
  set RC=%ERRORLEVEL%
  if %RC%==0 (
    echo Application responded successfully.
    goto smoke_test
  )
  set /a WAIT_SECONDS+=5
  if %WAIT_SECONDS% GEQ %MAX_WAIT% (
    echo Timed out waiting for the application (waited %MAX_WAIT% seconds). Check container logs with:
    echo     %COMPOSE_CMD% logs payara
    popd
    exit /b 2
  )
  ping -n 6 127.0.0.1 >nul
  goto wait_loop

:smoke_test
echo.
echo Running a basic smoke test (GET /quiz/buscarUsuarios)...
powershell -Command "try { $r = Invoke-WebRequest -UseBasicParsing -Uri 'http://localhost:8080/sisdat-web/quiz/buscarUsuarios' -TimeoutSec 10; Write-Host 'HTTP/' $r.StatusCode; Write-Host 'Body (first 400 chars):'; $s = $r.Content; if ($s.Length -gt 400) { $s.Substring(0,400) } else { $s } } catch { Write-Host 'Request failed or endpoint returned non-200'; exit 3 }"
if errorlevel 1 (
  echo Smoke test failed. Check logs and container status:
  echo     %COMPOSE_CMD% ps
  echo     %COMPOSE_CMD% logs payara --tail=200
  popd
  exit /b 3
)

echo.
echo SUCCESS: Application is up and responding at http://localhost:8080/sisdat-web/
echo To view logs: %COMPOSE_CMD% logs -f payara
echo To stop: %COMPOSE_CMD% down

popd
endlocal
exit /b 0
