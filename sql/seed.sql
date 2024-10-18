INSERT INTO guild (name) VALUES ('Media');
INSERT INTO guild (name) VALUES ('Technical');
INSERT INTO guild (name) VALUES ('HR');
INSERT INTO guild (name) VALUES ('Plan');

INSERT INTO guild_permission (name) VALUES ('Create');
INSERT INTO guild_permission (name) VALUES ('View');
INSERT INTO guild_permission (name) VALUES ('Edit');
INSERT INTO guild_permission (name) VALUES ('Delete');

INSERT INTO guild_role (name, guild_id) VALUES ('Member', 1);
INSERT INTO guild_role (name, guild_id) VALUES ('Leader', 1);
INSERT INTO guild_role (name, guild_id) VALUES ('Vice President', 1);

INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (1, 1);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (1, 2);

INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 1);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 2);
INSERT INTO guild_role_permission (guild_role_id, guild_permission_id) VALUES (2, 3);

INSERT INTO crew (name) VALUES ('BE1');
INSERT INTO crew (name) VALUES ('BE2');
INSERT INTO crew (name) VALUES ('BE3');
INSERT INTO crew (name) VALUES ('BE4');

INSERT INTO crew (name) VALUES ('FE1');
INSERT INTO crew (name) VALUES ('FE2');
INSERT INTO crew (name) VALUES ('FE3');

INSERT INTO crew (name) VALUES ('AI1');
INSERT INTO crew (name) VALUES ('AI2');
INSERT INTO crew (name) VALUES ('AI3');

INSERT INTO crew (name) VALUES ('GAME1');
INSERT INTO crew (name) VALUES ('GAME2');
INSERT INTO crew (name) VALUES ('GAME3');

INSERT INTO crew (name) VALUES ('SEC1');
INSERT INTO crew (name) VALUES ('SEC2');

INSERT INTO crew_permission (name) VALUES ('ViewTask');
INSERT INTO crew_permission (name) VALUES ('MakeTask');
INSERT INTO crew_permission (name) VALUES ('EditTask');
INSERT INTO crew_permission (name) VALUES ('DeleteTask');

INSERT INTO crew_role (name, crew_id) VALUES ('Member', 1);
INSERT INTO crew_role (name, crew_id) VALUES ('Leader', 1);
INSERT INTO crew_role (name, crew_id) VALUES ('Consultant', 1);
INSERT INTO crew_role (name, crew_id) VALUES ('Vice Consultant', 1);

INSERT INTO crew_role_permission (crew_role_id, crew_permission_id) VALUES (1, 1);
INSERT INTO crew_role_permission (crew_role_id, crew_permission_id) VALUES (1, 2);

INSERT INTO crew_role_permission (crew_role_id, crew_permission_id) VALUES (2, 1);
INSERT INTO crew_role_permission (crew_role_id, crew_permission_id) VALUES (2, 2);
INSERT INTO crew_role_permission (crew_role_id, crew_permission_id) VALUES (2, 3);

INSERT INTO permission (name) VALUES ('ViewUserInformation');
INSERT INTO permission (name) VALUES ('DeleteUserAccount');
INSERT INTO permission (name) VALUES ('EditUserAccount');
INSERT INTO permission (name) VALUES ('EditUserProfile');

INSERT INTO role (name) VALUES ('President');
INSERT INTO role (name) VALUES ('Member');
INSERT INTO role (name) VALUES ('Vice President');

INSERT INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 4);

INSERT INTO role_permission (role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 4);
