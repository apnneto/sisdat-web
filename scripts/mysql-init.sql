-- Set session charset
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- Ensure sisdat_schema uses utf8mb4 (overrides any latin1 default)
ALTER DATABASE sisdat_schema
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

-- Grant full privileges to sisdat user so it can manage schemas (DROP, CREATE, etc.)
GRANT ALL PRIVILEGES ON *.* TO 'sisdat'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;