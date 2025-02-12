INSERT INTO guild (name) VALUES ('Media');
INSERT INTO guild (name) VALUES ('Technical');

INSERT INTO crew (name) VALUES ('BE1');
INSERT INTO crew (name) VALUES ('BE2');

INSERT INTO permission (name) VALUES ("CrudRole"); 
INSERT INTO permission (name) VALUES ("CrudEvent"); 
INSERT INTO permission (name) VALUES ("CrudPermission"); 
INSERT INTO permission (name) VALUES ("SetUserRole"); 
INSERT INTO permission (name) VALUES ("UpdateUserRole"); 
INSERT INTO permission (name) VALUES ("AddPermissionToRole"); 
INSERT INTO permission (name) VALUES ("ViewUserInformation"); 
INSERT INTO permission (name) VALUES ("DeleteUserAccount");
INSERT INTO permission (name) VALUES ('ViewGuild');
INSERT INTO permission (name) VALUES ('CRUDGuild');
INSERT INTO permission (name) VALUES ('ViewCrew');
INSERT INTO permission (name) VALUES ('CRUDCrew');

INSERT INTO role (name) VALUES ("President");
INSERT INTO role (name) VALUES ("Member");

INSERT INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 4);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 5);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 6);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 7);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 8);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 9);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 10);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 11);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 12);


INSERT INTO generation(id) VALUES (19);
