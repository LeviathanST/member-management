INSERT INTO guild (name) VALUES ('Media');
INSERT INTO guild (name) VALUES ('Technical');

INSERT INTO guild_permission (name) VALUES ('ViewGuild');
INSERT INTO guild_permission (name) VALUES ('CRUDGuild');
INSERT INTO guild_permission (name) VALUES ('ViewGuildRole');
INSERT INTO guild_permission (name) VALUES ('CRUDGuildRole');
INSERT INTO guild_permission (name) VALUES ('ViewUserGuildRole');
INSERT INTO guild_permission (name) VALUES ('CRUDUserGuildRole');
INSERT INTO guild_permission (name) VALUES ('ViewGuildPermission');
INSERT INTO guild_permission (name) VALUES ('CRUDGuildPermission');
INSERT INTO guild_permission (name) VALUES ('ViewGuildRolePermission');
INSERT INTO guild_permission (name) VALUES ('CRUDGuildRolePermission');
INSERT INTO guild_permission (name) VALUES ('ViewGuildEvent');
INSERT INTO guild_permission (name) VALUES ('CRUDGuildEvent');


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