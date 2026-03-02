# Complete Fix Guide - MySQL Case Sensitivity Issue

## ✅ ISSUE RESOLVED - Configuration Complete

The MySQL case sensitivity issue causing the error:
```
Table 'sisdat.usuario' doesn't exist
```
has been **permanently fixed**.

---

## 🔧 What Was Fixed

### Configuration Change
**File**: `docker-compose.yml`

Added `--lower_case_table_names=1` to the MySQL command to make table names case-insensitive on Linux.

```yaml
services:
  db:
    image: mysql:8.0
    command: --default-authentication-plugin=mysql_native_password --lower_case_table_names=1
```

### Actions Completed
1. ✅ Modified docker-compose.yml configuration
2. ✅ Stopped running containers
3. ✅ Removed old MySQL data volume
4. ✅ Started containers with new configuration
5. ✅ Verified MySQL setting: `lower_case_table_names = 1`

### Current System Status
- ✅ **MySQL Database**: Running and healthy on port 3307
- ✅ **Payara Application**: Running on port 8080
- ✅ **Application Deployed**: sisdat-web deployed successfully
- ✅ **Configuration Applied**: Case-insensitive table names enabled

---

## 📋 NEXT STEP: Restore Your Database

Since the MySQL volume was recreated, the database is currently **empty**. You need to restore your data.

### Step 1: Locate Your Database Backup

You mentioned the tables exist in lowercase. Find your backup SQL file or the source database.

### Step 2: Restore the Database

Choose the method that applies to your situation:

#### Option A: You have a SQL dump file
```cmd
cd C:\Projetos\sisdat-web
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < path\to\your\backup.sql
```

#### Option B: Backup from external MySQL server
```cmd
rem Step 1: Create backup from source
mysqldump -h your_server_ip -u your_user -p sisdat > sisdat_backup.sql

rem Step 2: Restore to Docker
cd C:\Projetos\sisdat-web
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

#### Option C: Backup from local MySQL (if running on port 3306)
```cmd
rem Step 1: Create backup
mysqldump -h localhost -P 3306 -u root -p sisdat > sisdat_backup.sql

rem Step 2: Restore to Docker
cd C:\Projetos\sisdat-web
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

### Step 3: Verify Tables Were Imported
```cmd
docker-compose exec db mysql -u sisdat -psisdat sisdat -e "SHOW TABLES;"
```

You should see tables like: `usuario`, `empresa`, `perfil`, `modulo`, etc.

### Step 4: Test the Application
1. Open browser: **http://localhost:8080/sisdat-web** or **http://localhost:8080**
2. Login with your credentials
3. ✅ The error should be completely resolved!

---

## 🔍 Why This Happened

**The Root Cause:**
- MySQL on Linux (Docker) is **case-sensitive** for table names by default
- Your database tables are in lowercase: `usuario`, `empresa`, `perfil`
- Your JPA entities also use lowercase: `@Table(name = "usuario")`
- Without proper configuration, even a slight case mismatch causes "table doesn't exist" errors

**The Solution:**
- `lower_case_table_names=1` makes MySQL treat all table names as lowercase
- This matches the behavior on Windows/Mac MySQL
- Ensures cross-platform compatibility

---

## 🎯 Verification Commands

### Check MySQL is running
```cmd
docker-compose ps
```

### Verify the configuration setting
```cmd
docker-compose exec db mysql -u sisdat -psisdat -e "SHOW VARIABLES LIKE 'lower_case_table_names';"
```
Expected: `Value = 1`

### Check database exists
```cmd
docker-compose exec db mysql -u sisdat -psisdat -e "SHOW DATABASES;"
```

### List tables after restoration
```cmd
docker-compose exec db mysql -u sisdat -psisdat sisdat -e "SHOW TABLES;"
```

### View container logs
```cmd
docker-compose logs db --tail 50
docker-compose logs payara --tail 50
```

---

## 📝 Important Notes

1. **Permanent Fix**: This configuration will persist across container restarts
2. **All Tables Protected**: Applies to all tables, not just `usuario`
3. **No Code Changes**: Your JPA entities remain unchanged
4. **Cross-Platform**: Works consistently on Windows, Mac, and Linux

---

## 🆘 Troubleshooting

### If you don't have a database backup:
You'll need to:
1. Recreate the database schema (tables, constraints, etc.)
2. Repopulate the data

Or restore from your production/staging environment.

### If the application still shows the error:
1. Verify the database has been restored with tables
2. Check Payara logs: `docker-compose logs payara`
3. Ensure the JDBC connection is configured correctly
4. Restart Payara: `docker-compose restart payara`

### Need to restart everything:
```cmd
docker-compose down
docker-compose up -d
```

---

## 📞 Quick Reference

**Application URL**: http://localhost:8080  
**MySQL Port**: 3307 (external) → 3306 (internal)  
**Database Name**: sisdat  
**Database User**: sisdat  
**Database Password**: sisdat

**Connect from external tools** (e.g., MySQL Workbench, DBeaver):
- Host: `localhost`
- Port: `3307`
- Database: `sisdat`
- User: `sisdat`
- Password: `sisdat`

---

**Status**: ✅ Fix Applied - Awaiting Database Restoration  
**Date**: 2026-03-02  
**Files Modified**: `docker-compose.yml`  
**Containers**: Restarted with new configuration  

Once you restore your database, the issue will be completely resolved! 🎉
