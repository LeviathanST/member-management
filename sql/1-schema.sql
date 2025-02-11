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
	sex ENUM ('MALE', 'FEMALE') NOT NULL,
	student_code VARCHAR(8) NOT NULL,
	email VARCHAR(255) NOT NULL,
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
	account_id CHAR(36) NOT NULL PRIMARY KEY,
	role_id INTEGER UNSIGNED NOT NULL,

	FOREIGN KEY (account_id) REFERENCES user_account(id),
	FOREIGN KEY (role_id) REFERENCES role(id)
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
-- Trigger to check count_mistake
DELIMITER //

CREATE TRIGGER before_update_account
BEFORE UPDATE ON user_account
FOR EACH ROW
BEGIN 
	IF NEW.count_mistake != OLD.count_mistake THEN
        IF NEW.count_mistake < 0 OR NEW.count_mistake > 3 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Mistake counting must be between 0 and 3!!!";
        END IF;
    END IF;
END;
//

DELIMITER ;
