# Run SisDAT locally with Docker (Payara + MySQL)

This project includes a Docker Compose setup (Payara + MySQL) to run the application locally for testing. The helper script `scripts/run-docker-windows.bat` automates building the WAR, starting the stack, waiting for the app, and running a smoke test.

## Why use MySQL in Docker instead of localhost?

- **Isolation** – The container runs independently, avoiding conflicts with other local MySQL instances or configurations.
- **Consistency** – Everyone on the team uses the same MySQL version and configuration, eliminating "works on my machine" issues.
- **Easy setup/teardown** – Spin up or destroy the database with a single command, and reset to a clean state at any time.
- **No local installation required** – No need to install and configure MySQL directly on Windows.
- **Port flexibility** – The container maps to port `3307` locally, avoiding conflicts with any existing MySQL instance on `3306`.
- **Reproducibility** – The entire environment is defined in `docker-compose.yml`, version-controlled and reproducible.

---

## Persistent database

The MySQL data is stored in a **named Docker volume** (`db-data`), which means:

| Feature | How it's handled |
|---|---|
| **Data persistence** | Named volume `db-data` mapped to `/var/lib/mysql` |
| **WAR redeployment** | Only the `payara` service is affected; `db` container and its volume are untouched |
| **Container restart** | `restart: unless-stopped` ensures MySQL comes back up automatically |
| **Init script** | `mysql-init.sql` only runs once on first volume creation — never again |

To redeploy **only the WAR** without touching the database:
```bat
docker-compose up -d --build payara
```

To fully reset the database (⚠️ deletes all data):
```bat
docker-compose down
docker volume rm sisdat-web_db-data
docker-compose up -d
```

---

## Prerequisites
- Docker Desktop installed and running (Windows)
- Java + Maven (for building the WAR)
- Powershell available (Windows default)

## Quick start (Windows)
1. Open a cmd.exe with Administrator rights (recommended).
2. From the project root run:
```bat
scripts\run-docker-windows.bat
```
3. The script will:
   - Build the WAR with `mvn -DskipTests package`
   - Build and start the docker-compose stack (MySQL + Payara)
   - Wait up to 3 minutes for the app to respond and perform a GET smoke test on `/quiz/buscarUsuarios`

## Common troubleshooting
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

## Stopping the stack
```bat
docker-compose down
```

If you prefer not to run Docker, you can deploy the WAR to a local Payara or GlassFish server using `scripts/deploy-war.bat` and create the datasource with `scripts/configure-glassfish-datasource.bat`.

If you want me to also add a Linux/macOS helper script, tell me and I will add `scripts/run-docker.sh`.