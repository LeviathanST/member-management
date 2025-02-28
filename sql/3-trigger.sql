DELIMITER //
CREATE TRIGGER after_insert_new_crew
AFTER INSERT ON crew
FOR EACH ROW
BEGIN
	DECLARE leader_role_name VARCHAR(255);
	DECLARE leader_id CHAR(36);
	DECLARE new_role_id INT;

	SET leader_role_name = CONCAT(
			(SELECT code FROM crew
			WHERE code = REGEXP_REPLACE(NEW.code, '[0-9]+', '')
			), '_Leader');
	IF leader_role_name IS NOT NULL THEN
		SELECT id INTO new_role_id FROM role
		WHERE name = CONCAT(NEW.code, '_Leader')
		LIMIT 1;

		IF new_role_id IS NOT NULL THEN
			INSERT INTO user_role (account_id, role_id)
			SELECT account_id, new_role_id
			FROM user_role ur
			JOIN role r ON r.id = ur.role_id
			WHERE r.name = leader_role_name;
		END IF;
	END IF;
END //
DELIMITER ;
