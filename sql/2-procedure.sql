
-- Procedure to remove specified role and reassign all affected users to a default role
DELIMITER //
CREATE PROCEDURE removeRoleAndReassigned (
    IN prefix VARCHAR(9),
    IN role_name VARCHAR(255),
    OUT rows_affected INT
) 
BEGIN
    DECLARE v_role_id INT;
    DECLARE v_new_role_id INT;
    DECLARE v_affected INT DEFAULT 0;
    
    START TRANSACTION;

    SELECT id INTO v_role_id FROM role WHERE name = role_name AND is_default = false LIMIT 1;
    SELECT id INTO v_new_role_id FROM role WHERE name LIKE CONCAT(prefix, '_%') AND is_default = TRUE LIMIT 1;

    IF v_role_id IS NULL THEN
        ROLLBACK;
        SET rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Role to remove not found or is default';
    END IF;
    IF v_new_role_id IS NULL THEN
        ROLLBACK;
        SET rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No default role found for reassignment';
    END IF;

    CREATE TEMPORARY TABLE IF NOT EXISTS user_affected (
        account_id CHAR(36)
    );

    INSERT INTO user_affected (account_id)
    SELECT ua.id
    FROM role r
    JOIN user_role ur ON ur.role_id = r.id
    JOIN user_account ua ON ua.id = ur.account_id
    WHERE r.name = role_name;

    DELETE FROM role
    WHERE id = v_role_id;

    INSERT INTO user_role (account_id, role_id)
    SELECT account_id, v_new_role_id
    FROM user_affected;

    SET rows_affected = ROW_COUNT();

    DROP TEMPORARY TABLE IF EXISTS user_affected;
    COMMIT;
END //

CREATE PROCEDURE createParty (
    IN context ENUM('guild', 'crew'),
    IN name VARCHAR(255),
    IN code VARCHAR(9),
    IN username VARCHAR(255)
)
BEGIN
    DECLARE v_exist BOOLEAN DEFAULT FALSE;
    DECLARE v_new_leader_role_id INT;
    DECLARE v_new_permission_id INT;


    START TRANSACTION;
        IF context IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Context is null!';
        END IF;
        IF name IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Name is null!';
        END IF;
        IF code IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Code is null!';
        END IF;

        CASE context 
            WHEN 'guild' THEN
                SET v_exist = EXISTS (
                    SELECT 1
                    FROM guild
                    WHERE guild.name = name
                );
                IF v_exist THEN
                    ROLLBACK;
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Guild name already exists';
                END IF;

                SET v_exist = EXISTS (
                    SELECT 1
                    FROM guild
                    WHERE guild.code = code
                );
                IF v_exist THEN
                    ROLLBACK;
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Guild code already exists';
                END IF;

                INSERT INTO role (name)
                VALUES (CONCAT(code, '_Leader'));
                SET v_new_leader_role_id = LAST_INSERT_ID();

                INSERT INTO role (is_default, name)
                VALUES (true, CONCAT(code, '_Member'));

                INSERT INTO guild (code, name)
                VALUES (code, name);
            WHEN 'crew' THEN
                SET v_exist = EXISTS (
                    SELECT 1
                    FROM crew
                    WHERE crew.name = name
                );
                IF v_exist THEN
                    ROLLBACK;
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Crew name already exists';
                END IF;

                SET v_exist = EXISTS (
                    SELECT 1
                    FROM crew
                    WHERE crew.code = code 
                );
                IF v_exist THEN
                    ROLLBACK;
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Crew code already exists';
                END IF;

                INSERT INTO role (name)
                VALUES (CONCAT(code, '_Leader'));
                SET v_new_leader_role_id = LAST_INSERT_ID();

                INSERT INTO role (is_default, name)
                VALUES (true, CONCAT(code, '_Member'));

                INSERT INTO crew (code, name)
                VALUES (code, name);
            ELSE
                ROLLBACK;
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid context!';
        END CASE;

        SET v_exist = EXISTS (
            SELECT 1
            FROM user_account
            WHERE user_account.username = username
        );
        IF NOT v_exist THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exists!';
        END IF;

        SET v_exist = EXISTS (
            SELECT 1
            FROM role r
            JOIN user_role ur ON ur.role_id = r.id
            JOIN user_account ua ON ua.id = ur.account_id
            WHERE ua.username = username AND r.name = CONCAT(code, '_Leader')
        );

        IF NOT v_exist THEN
            INSERT INTO user_role (account_id, role_id)
            VALUES (
                (SELECT id FROM user_account ua WHERE ua.username = username),
                v_new_leader_role_id
            );
        END IF;

        INSERT INTO permission (name)
        VALUES (CONCAT(code, '.*'));
        SET v_new_permission_id = LAST_INSERT_ID();
        
        INSERT into role_permission (role_id, permission_id)
        VALUES (
            v_new_leader_role_id,
            v_new_permission_id
        );
    COMMIT;
END //

CREATE PROCEDURE removeParty(IN context ENUM('guild', 'crew'), IN prefix VARCHAR(9))
BEGIN
    DECLARE v_exist BOOLEAN DEFAULT FALSE;

    START TRANSACTION;
        IF prefix IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Code is null';
        END IF;
        IF context IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Context is null';
        END IF;
        CASE context 
            WHEN 'guild' THEN
                SET v_exist = EXISTS (
                    SELECT 1
                    FROM guild
                    WHERE guild.code = prefix 
                );
                IF NOT v_exist THEN
                    ROLLBACK;
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Guild does not exists';
                END IF;

                DELETE FROM guild 
                WHERE guild.code = prefix;
            WHEN 'crew' THEN
                SET v_exist = EXISTS (
                    SELECT 1
                    FROM crew
                    WHERE crew.code = prefix 
                );
                IF NOT v_exist THEN
                    ROLLBACK;
                    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Crew does not exists';
                END IF;

                DELETE FROM crew
                WHERE crew.code LIKE CONCAT(prefix, "%");
            ELSE
                ROLLBACK;
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid context!';
        END CASE;

        DELETE FROM permission
        WHERE name = CONCAT(prefix, '%');

        DELETE FROM role
        WHERE name LIKE CONCAT(prefix, '%');

        DROP TEMPORARY TABLE IF EXISTS role_affected;
    COMMIT;
END //
DELIMITER ;
