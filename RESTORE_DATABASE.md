# Database Restoration Instructions

## ✅ Configuration Complete!

The MySQL server is now running with `lower_case_table_names=1`, which means table names are case-insensitive.

Verification:
```
Variable: lower_case_table_names = 1
```

## Next Step: Restore Your Database

Since the database volume was recreated, you need to restore your database backup.

### If you have a SQL dump file:

```cmd
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < your_backup_file.sql
```

### If you're restoring from an external MySQL server:

```cmd
mysqldump -h your_server -u your_user -p sisdat > sisdat_backup.sql
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

### If you're restoring from a local MySQL instance on port 3306:

```cmd
mysqldump -h localhost -u root -p sisdat > sisdat_backup.sql
docker-compose exec -T db mysql -u sisdat -psisdat sisdat < sisdat_backup.sql
```

## After Restoration

Once your database is restored:

1. Access the application at: http://localhost:8080
2. Try logging in
3. The "Table 'sisdat.usuario' doesn't exist" error should be completely resolved!

## Containers Status

- ✅ MySQL (db): Running and healthy on port 3307
- ✅ Payara (application): Running on port 8080
- ✅ lower_case_table_names: Enabled (value = 1)

The case-sensitivity issue is now permanently fixed in the Docker configuration!
