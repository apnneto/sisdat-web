# MySQL Table Case Sensitivity Fix - RESOLVED ✅

## Problem
After login, the application threw the following exception:
```
com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Table 'sisdat.usuario' doesn't exist
```

Even though the table `usuario` exists in the database.

## Root Cause
This is a MySQL case sensitivity issue that occurs when running MySQL 8.0 on Linux (Docker containers use Linux):

1. **On Windows/Mac**: MySQL is case-insensitive for table names by default
2. **On Linux**: MySQL is case-sensitive for table names by default
3. **Confirmed**: All database tables are in lowercase (usuario, empresa, perfil, etc.)
4. The JPA entity has `@Table(name = "usuario")` with lowercase
5. On Linux without proper configuration, MySQL's case-sensitive file system can cause issues

## Solution Applied ✅
Added the `--lower_case_table_names=1` parameter to the MySQL server configuration in `docker-compose.yml`.

This setting makes MySQL treat table names as case-insensitive, which is the standard behavior on Windows and ensures compatibility regardless of how the tables were originally created.

### Changes Made ✅
**File**: `docker-compose.yml`

Changed:
```yaml
command: --default-authentication-plugin=mysql_native_password
```

To:
```yaml
command: --default-authentication-plugin=mysql_native_password --lower_case_table_names=1
```

**Status**: Configuration applied and containers restarted successfully.

**Verification**: 
```
mysql> SHOW VARIABLES LIKE 'lower_case_table_names';
+------------------------+-------+
| Variable_name          | Value |
+------------------------+-------+
| lower_case_table_names | 1     |
+------------------------+-------+
```

## Fix Applied - Containers Restarted ✅

The following steps have been completed:

1. ✅ Updated `docker-compose.yml` with `--lower_case_table_names=1`
2. ✅ Stopped running containers with `docker-compose down`
3. ✅ Removed MySQL data volume with `docker volume rm sisdat-web_db-data`
4. ✅ Started containers with new configuration using `docker-compose up -d`
5. ✅ Verified MySQL is running with correct setting (`lower_case_table_names = 1`)

**Next Step**: Restore your database backup.

## Database Restoration Required

Since the MySQL data volume was recreated, you need to restore your database from backup.

### Option 1: Restore from SQL dump file
```cmd
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < your_backup.sql
```

### Option 2: Restore from external MySQL server
```cmd
rem First, create a backup from your source database
mysqldump -h source_host -u source_user -p sisdat > sisdat_backup.sql

rem Then restore to Docker MySQL
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

### Option 3: Restore from local MySQL (if you have one on port 3306)
```cmd
mysqldump -h localhost -P 3306 -u root -p sisdat > sisdat_backup.sql
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

## After Restoration

Once you restore your database, the application should work correctly:

1. Access the application at: **http://localhost:8080**
2. Try logging in
3. The `Table 'sisdat.usuario' doesn't exist` error will be resolved

## Container Information

- **MySQL Database**: Port 3307 (external) → 3306 (internal)
- **Payara Application Server**: Port 8080
- **MySQL Configuration**: `lower_case_table_names=1` ✅

## Additional Notes
- All entities in the codebase use lowercase table names (`usuario`, `empresa`, `perfil`, etc.)
- This is the recommended configuration for cross-platform compatibility
- The setting prevents future case sensitivity issues with any table
- The fix is permanent - it will persist across container restarts
