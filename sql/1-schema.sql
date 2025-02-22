CREATE TABLE IF NOT EXISTS generation (
	id INTEGER UNSIGNED PRIMARY KEY 
);
CREATE TABLE IF NOT EXISTS user_account (
	id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
	username VARCHAR(255) UNIQUE NOT NULL,
	hashed_password VARCHAR(255) NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	count_mistake INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS user_profile (
	account_id CHAR(36) PRIMARY KEY,
	full_name VARCHAR(255) NOT NULL,
	sex ENUM ('MALE', 'FEMALE', 'NONE') NOT NULL DEFAULT 'NONE',
	student_code VARCHAR(8) NOT NULL,
	email VARCHAR(255) NOT NULL,
 	contact_email VARCHAR(255),
	generation_id INTEGER UNSIGNED NOT NULL,
	dob DATE NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (generation_id) REFERENCES generation(id)
);

CREATE TABLE IF NOT EXISTS crew (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	code VARCHAR(9) NOT NULL,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS guild (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	code VARCHAR(9) NOT NULL,
	name VARCHAR(255) UNIQUE NOT NULL
);

-- ROLE 
-- + APPLICATION
CREATE TABLE IF NOT EXISTS permission (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	context ENUM('app', 'guild', 'crew') NOT NULL DEFAULT 'app',

	UNIQUE(name, context)
);

CREATE TABLE IF NOT EXISTS role (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	is_default BOOLEAN NOT NULL DEFAULT false,
	name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS role_permission (
	role_id INTEGER UNSIGNED NOT NULL,
	permission_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (role_id, permission_id),
	FOREIGN KEY (permission_id) REFERENCES permission(id),
	FOREIGN KEY (role_id) REFERENCES role(id)
);
CREATE TABLE IF NOT EXISTS user_role (
	account_id CHAR(36) NOT NULL,
	role_id INTEGER UNSIGNED NOT NULL,

	PRIMARY KEY (account_id, role_id),
	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (role_id) REFERENCES role(id)
);
-- END permission

-- EVENT
-- + Crew
CREATE TABLE IF NOT EXISTS crew_event (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	crew_id INTEGER UNSIGNED NOT NULL,
	title VARCHAR(255) NOT NULL,
	description TEXT,
	generation_id INTEGER UNSIGNED NOT NULL,
	start_at TIMESTAMP NOT NULL,
	end_at TIMESTAMP NOT NULL,
	type VARCHAR(255) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (crew_id) REFERENCES crew(id),
	FOREIGN KEY (generation_id) REFERENCES generation(id)
);

-- + Guild
CREATE TABLE IF NOT EXISTS guild_event (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	guild_id INTEGER UNSIGNED NOT NULL,
	title VARCHAR(255) NOT NULL,
	description TEXT,
	generation_id INTEGER UNSIGNED NOT NULL,
	start_at TIMESTAMP NOT NULL,
	end_at TIMESTAMP NOT NULL,
	type VARCHAR(255) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	FOREIGN KEY (guild_id) REFERENCES guild(id),
	FOREIGN KEY (generation_id) REFERENCES generation(id)
);
-- Application
CREATE TABLE IF NOT EXISTS event (
    id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    generation_id INTEGER UNSIGNED NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    type VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (generation_id) REFERENCES generation(id)
);

