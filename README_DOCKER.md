# Run SisDAT locally with Docker (Payara + MySQL)

This project includes a Docker Compose setup (Payara + MySQL) to run the application locally for testing. The helper script `scripts/run-docker-windows.bat` automates building the WAR, starting the stack, waiting for the app, and running a smoke test.

Prerequisites
- Docker Desktop installed and running (Windows)
- Java + Maven (for building the WAR)
- Powershell available (Windows default)

Quick start (Windows)
1. Open a cmd.exe with Administrator rights (recommended).
2. From the project root run:
```bat
scripts\run-docker-windows.bat
```
3. The script will:
   - Build the WAR with `mvn -DskipTests package`
   - Build and start the docker-compose stack (MySQL + Payara)
   - Wait up to 3 minutes for the app to respond and perform a GET smoke test on `/quiz/buscarUsuarios`

Common troubleshooting
- Docker command not found: make sure Docker Desktop is installed and `docker` is on PATH.
- Compose command not found: the script uses either `docker-compose` or `docker compose`. Install Docker Compose or use Docker CLI v2 that includes `docker compose`.
- Application not responding within timeout: inspect container logs:
```bat
# show logs for payara
docker-compose logs payara --tail=200
```
or follow logs live:
```bat
docker-compose logs -f payara
```
- Database initialization: the compose file maps `./META-INF/script.sql` into the MySQL init directory. If MySQL data exists in a local volume, remove the `db-data` docker volume to reinitialize.

Stopping the stack
```bat
docker-compose down
```

If you prefer not to run Docker, you can deploy the WAR to a local Payara or GlassFish server using `scripts/deploy-war.bat` and create the datasource with `scripts/configure-glassfish-datasource.bat`.

If you want me to also add a Linux/macOS helper script, tell me and I will add `scripts/run-docker.sh`.
