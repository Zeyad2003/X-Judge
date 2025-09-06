-- Create database if it doesn't exist (though MYSQL_DATABASE should handle this)
CREATE DATABASE IF NOT EXISTS `X-Judge`;

-- Create user if it doesn't exist (though MYSQL_USER should handle this)
CREATE USER IF NOT EXISTS 'judge'@'%' IDENTIFIED BY 'judge';

-- Grant all privileges on the X-Judge database to the judge user
GRANT ALL PRIVILEGES ON `X-Judge`.* TO 'judge'@'%';

-- Flush privileges to ensure changes take effect
FLUSH PRIVILEGES;

-- Use the X-Judge database
USE `X-Judge`;
