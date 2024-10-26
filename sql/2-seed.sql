INSERT INTO guild (name) VALUES ('Media');
INSERT INTO guild (name) VALUES ('Technical');

INSERT INTO guild_permission (name) VALUES ('View');
INSERT INTO guild_permission (name) VALUES ('Edit');
INSERT INTO guild_permission (name) VALUES ('Delete');

INSERT INTO guild_role (name, guild_id) VALUES ('Member', 1);
INSERT INTO guild_role (name, guild_id) VALUES ('Leader', 1);

INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (1, 1);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (1, 2);

INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 1);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 2);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 3);

INSERT INTO crew (name) VALUES ('BE1');
INSERT INTO crew (name) VALUES ('BE2');

INSERT INTO crew_permission (name) VALUES ("ViewTask"); 
INSERT INTO crew_permission (name) VALUES ("MakeTask"); 
INSERT INTO crew_permission (name) VALUES ("EditTask"); 
INSERT INTO crew_permission (name) VALUES ("DeleteTask"); 

INSERT INTO permission (name) VALUES ("ViewUserInformation"); 
INSERT INTO permission (name) VALUES ("DeleteUserAccount"); 
INSERT INTO permission (name) VALUES ("EditUserAccount"); 
INSERT INTO permission (name) VALUES ("EditUserProfile"); 

INSERT INTO role (name) VALUES ("President");
INSERT INTO role (name) VALUES ("Member");

INSERT INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 4);

INSERT INTO role_permission (role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 4);
