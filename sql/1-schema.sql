CREATE TABLE IF NOT EXISTS generation (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(5) NOT NULL
);
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
	account_id CHAR(36) UNIQUE PRIMARY KEY,
	full_name VARCHAR(255) NOT NULL,
	sex ENUM ('MALE', 'FEMALE') NOT NULL,
	student_code VARCHAR(8) NOT NULL,
	contact_email VARCHAR(255),
	generation_id INTEGER UNSIGNED NOT NULL,
	dob DATE NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (generation_id) REFERENCES generation(id)
);

-- ROLE 
-- + APPLICATION
CREATE TABLE IF NOT EXISTS permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS role_permission (
	role_id INTEGER UNSIGNED NOT NULL,
	permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (role_id, permission_id),
	FOREIGN KEY (permission_id) REFERENCES permission(id),
	FOREIGN KEY (role_id) REFERENCES role(id)
);
CREATE TABLE IF NOT EXISTS user_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	account_id CHAR(36) NOT NULL, role_id INTEGER UNSIGNED NOT NULL,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (role_id) REFERENCES role(id)
);
-- + CREW
CREATE TABLE IF NOT EXISTS crew (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS crew_permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS crew_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	crew_id INTEGER UNSIGNED NOT NULL,

	UNIQUE KEY (name, crew_id),
	FOREIGN KEY (crew_id) REFERENCES crew(id)
);
CREATE TABLE IF NOT EXISTS crew_role_permission (
	crew_role_id INTEGER UNSIGNED NOT NULL,
	crew_permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (crew_role_id, crew_permission_id),
	FOREIGN KEY (crew_role_id) REFERENCES crew_role(id),
	FOREIGN KEY (crew_permissioN_id) REFERENCES crew_permission(id)
);
CREATE TABLE IF NOT EXISTS user_crew_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	account_id CHAR(36) NOT NULL,
	crew_role_id INTEGER UNSIGNED NOT NULL,

	UNIQUE KEY (account_id, crew_role_id),
	FOREIGN KEY (account_id) REFERENCES user_account(id), FOREIGN KEY (crew_role_id) REFERENCES crew_role(id)
);
-- + GUILD
CREATE TABLE IF NOT EXISTS guild (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS guild_permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS guild_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	guild_id INTEGER UNSIGNED NOT NULL,

	UNIQUE KEY (name, guild_id),
	FOREIGN KEY (guild_id) REFERENCES guild(id)
);
CREATE TABLE IF NOT EXISTS guild_role_permission (
	guild_role_id INTEGER UNSIGNED NOT NULL,
	guild_permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (guild_role_id, guild_permission_id),
	FOREIGN KEY (guild_role_id) REFERENCES guild_role(id),
	FOREIGN KEY (guild_permission_id) REFERENCES guild_permission(id)
);

CREATE TABLE IF NOT EXISTS user_guild_role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	account_id CHAR(36) NOT NULL,
	guild_role_id INTEGER UNSIGNED NOT NULL,

	UNIQUE KEY (account_id, guild_role_id),
	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (guild_role_id) REFERENCES guild_role(id)
);
-- END permission

-- EVENT
-- + Crew
CREATE TABLE IF NOT EXISTS crew_event (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	crew_id INTEGER UNSIGNED NOT NULL,
	title VARCHAR(255) NOT NULL,
	description VARCHAR(2048),
	generation_id INTEGER UNSIGNED NOT NULL,
	start_at TIMESTAMP NOT NULL,
	end_at TIMESTAMP NOT NULL,
	type VARCHAR(255) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (crew_id) REFERENCES crew(id),
	FOREIGN KEY (generation_id) REFERENCES generation(id)
);


CREATE TABLE IF NOT EXISTS crew_task(
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	crew_event_id INTEGER UNSIGNED NOT NULL,
	author_id VARCHAR(36) NOT NULL,
	title VARCHAR(255) NOT NULL,
	description VARCHAR(2048),
	start_at TIMESTAMP NOT NULL,
	end_at TIMESTAMP NOT NULL,
	update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (crew_event_id) REFERENCES crew_event(id),
	FOREIGN KEY (author_id) REFERENCES user_account(id)

);

CREATE TABLE IF NOT EXISTS user_task_crew (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	crew_task_id INTEGER UNSIGNED,
	assignee_account_id VARCHAR(36),

	FOREIGN KEY (assignee_account_id) REFERENCES user_account(id),
	FOREIGN KEY (crew_task_id) REFERENCES crew_task(id)
);
-- + Guild
CREATE TABLE IF NOT EXISTS guild_event (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	guild_id INTEGER UNSIGNED NOT NULL,
	title VARCHAR(255) NOT NULL,
	description VARCHAR(2048),
	generation_id INTEGER UNSIGNED NOT NULL,
	start_at TIMESTAMP NOT NULL,
	end_at TIMESTAMP NOT NULL,
	type VARCHAR(255) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (guild_id) REFERENCES guild(id),
	FOREIGN KEY (generation_id) REFERENCES generation(id)
);


CREATE TABLE IF NOT EXISTS guild_task (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	guild_event_id INTEGER UNSIGNED NOT NULL,
	author_id VARCHAR(36) NOT NULL,
	title VARCHAR(255) NOT NULL,
	description VARCHAR(2048),
	start_at TIMESTAMP NOT NULL,
	end_at TIMESTAMP NOT NULL,
	update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (guild_event_id) REFERENCES guild_event(id),
	FOREIGN KEY (author_id) REFERENCES user_account(id)

);

CREATE TABLE IF NOT EXISTS user_task_guild(
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	guild_task_id INTEGER UNSIGNED,
	assignee_account_id VARCHAR(36),

	FOREIGN KEY (assignee_account_id) REFERENCES user_account(id),
	FOREIGN KEY (guild_task_id) REFERENCES guild_task(id)
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