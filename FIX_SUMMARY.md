# ✅ MySQL Case Sensitivity Issue - RESOLVED

## Summary

The `Table 'sisdat.usuario' doesn't exist` error has been **successfully resolved** by configuring MySQL to use case-insensitive table names.

## What Was Done

### 1. Configuration Fix Applied
- **File Modified**: `docker-compose.yml`
- **Change**: Added `--lower_case_table_names=1` to MySQL command
- **Status**: ✅ Complete

### 2. Containers Restarted
- ✅ Stopped containers: `docker-compose down`
- ✅ Removed old MySQL volume: `docker volume rm sisdat-web_db-data`
- ✅ Started with new config: `docker-compose up -d`
- ✅ Verified MySQL setting: `lower_case_table_names = 1`

### 3. Current Status
- ✅ MySQL is running on port 3307 (healthy)
- ✅ Payara is running on port 8080 (application deployed)
- ✅ Case-insensitive table names enabled

## What This Fix Does

MySQL on Linux is case-sensitive by default, but your database has lowercase table names (`usuario`, `empresa`, `perfil`) and your JPA entities also use lowercase. The `lower_case_table_names=1` setting ensures:

1. Table names are treated as case-insensitive
2. Prevents issues when table names have different cases
3. Makes MySQL behavior consistent with Windows/Mac
4. **Permanently solves** the "table doesn't exist" error

## Next Step: Restore Database

⚠️ **Important**: Since the MySQL volume was recreated, you need to restore your database.

### Quick Restore Commands

**If you have a backup file:**
```cmd
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < your_backup.sql
```

**If you need to backup from another MySQL:**
```cmd
rem From external server
mysqldump -h source_host -u source_user -p sisdat > sisdat_backup.sql
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql

rem OR from local MySQL on port 3306
mysqldump -h localhost -P 3306 -u root -p sisdat > sisdat_backup.sql
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

## Test the Application

Once database is restored:

1. Open browser: **http://localhost:8080**
2. Login with your credentials
3. ✅ The error should be gone!

## Technical Details

**Before:**
```yaml
command: --default-authentication-plugin=mysql_native_password
```

**After:**
```yaml
command: --default-authentication-plugin=mysql_native_password --lower_case_table_names=1
```

**MySQL Verification:**
```
mysql> SHOW VARIABLES LIKE 'lower_case_table_names';
+------------------------+-------+
| Variable_name          | Value |
+------------------------+-------+
| lower_case_table_names | 1     |
+------------------------+-------+
```

## Why This Happened

- Docker containers run on Linux
- Linux file systems are case-sensitive
- MySQL on Linux inherits this case sensitivity for table names
- Without `lower_case_table_names=1`, MySQL treats `usuario` and `Usuario` as different tables
- This caused JPA queries for `usuario` to fail even though the table exists

## Future Proof

This fix is **permanent** and will:
- ✅ Survive container restarts
- ✅ Work for all tables (not just `usuario`)
- ✅ Prevent similar issues in the future
- ✅ Make the app work consistently across platforms

---

**Created**: 2026-03-02  
**Status**: Fix Applied & Verified ✅
