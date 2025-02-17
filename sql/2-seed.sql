INSERT INTO user_account (username, hashed_password) VALUES 
	('president', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42'),
	('member', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42');

INSERT INTO guild (name) VALUES ('Media');
INSERT INTO guild (name) VALUES ('Technical');

INSERT INTO crew (name) VALUES ('BE1');
INSERT INTO crew (name) VALUES ('BE2');

-- Application
INSERT INTO permission (name) VALUES ('app.role.crud'); 
INSERT INTO permission (name) VALUES ('app.role.add_permission'); 
INSERT INTO permission (name) VALUES ('app.news.crud'); 
INSERT INTO permission (name) VALUES ('app.permission.crud'); 
INSERT INTO permission (name) VALUES ('app.user_role.set'); 
INSERT INTO permission (name) VALUES ('app.user_role.update'); 
INSERT INTO permission (name) VALUES ('app.user.view_information'); -- Can view any account information
INSERT INTO permission (name) VALUES ('app.user.view_information_self'); -- Just see self information 
INSERT INTO permission (name) VALUES ('app.user.delete_account'); -- Can delete any account
INSERT INTO permission (name) VALUES ('app.user.profile.update'); -- Can delete any account

INSERT INTO role (name) VALUES ('President');
INSERT INTO role (name) VALUES ('Member');

INSERT INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 10);

-- Guild
INSERT INTO permission (name) VALUES ('guild.view');
INSERT INTO permission (name) VALUES ('guild.crud');

INSERT INTO role (name) VALUES ('BE_Leader');
INSERT INTO role (name) VALUES ('BE_ViceConsultant');

-- Crew
INSERT INTO permission (name) VALUES ('crew.view');
INSERT INTO permission (name) VALUES ('crew.crud');


INSERT INTO generation(id) VALUES (19);
