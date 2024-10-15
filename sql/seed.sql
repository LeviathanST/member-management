INSERT INTO guild (name) VALUES ('be1');
INSERT INTO guild (name) VALUES ('be2');

INSERT INTO guild_permission (name) VALUES ('view');
INSERT INTO guild_permission (name) VALUES ('edit');
INSERT INTO guild_permission (name) VALUES ('delete');

INSERT INTO guild_role (name, guild_id) VALUES ('member', 1);
INSERT INTO guild_role (name, guild_id) VALUES ('leader', 1);

INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (1, 1);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (1, 2);

INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 1);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 2);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 3);
