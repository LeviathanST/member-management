-- TODO: impl uuid for id 

CREATE TABLE IF NOT EXISTS user_account (
	id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
	username VARCHAR(255) UNIQUE NOT NULL,
	hashed_password VARCHAR(255) NOT NULL,
	email VARCHAR(255),
	is_active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	count_mistake INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS user_profile (
	id CHAR(36) PRIMARY KEY,
	account_id CHAR(36) UNIQUE,
	full_name VARCHAR(255) NOT NULL,
	sex ENUM ('MALE', 'FEMALE') NOT NULL,
	student_code VARCHAR(8) NOT NULL,
	contact_email VARCHAR(255),
	generation VARCHAR(3) NOT NULL,
	dob DATE NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (account_id) REFERENCES user_account(id)
);

CREATE TABLE IF NOT EXISTS guild (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS crew (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS crew_permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS guild_permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS role (
	name VARCHAR(255) NOT NULL,
	permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (name, permission_id),
	FOREIGN KEY (permission_id) REFERENCES permission(id)
);
CREATE TABLE IF NOT EXISTS guild_role (
	name VARCHAR(255) NOT NULL,
	guild_id INTEGER UNSIGNED,
	guild_permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (name, guild_id, guild_permission_id),
	FOREIGN KEY (guild_id) REFERENCES guild(id),
	FOREIGN KEY (guild_permission_id) REFERENCES guild_permission(id)
);
CREATE TABLE IF NOT EXISTS crew_role (
	name VARCHAR(255) NOT NULL,
	crew_id INTEGER UNSIGNED,
	crew_permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (name, crew_id, crew_permission_id),
	FOREIGN KEY (crew_id) REFERENCES crew(id),
	FOREIGN KEY (crew_permission_id) REFERENCES crew_permission(id)
);

CREATE TABLE IF NOT EXISTS user_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	account_id CHAR(36) NOT NULL,
	role_name VARCHAR(255) NOT NULL,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (role_name) REFERENCES role(name)
);

CREATE TABLE IF NOT EXISTS user_crew_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	account_id CHAR(36) NOT NULL,
	crew_role_name VARCHAR(255) NOT NULL,
	crew_id VARCHAR(255) NOT NULL,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (crew_role_name) REFERENCES crew_role(name)
);

CREATE TABLE IF NOT EXISTS user_guild_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	account_id CHAR(36) NOT NULL,
	guild_role_name VARCHAR(255) NOT NULL,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (guild_role_name) REFERENCES guild_role(name)
);

-- Trigger to check count_mistake
DELIMITER //

CREATE TRIGGER before_update_account
BEFORE UPDATE ON user_account
FOR EACH ROW
BEGIN 
	if NEW.count_mistake <= 0 or NEW.count_mistake > 3 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = "Mistake counting must be between 0 and 3";
	END IF;	
END;
//

DELIMITER ;
