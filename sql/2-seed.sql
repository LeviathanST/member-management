INSERT INTO user_account (username, hashed_password) VALUES 
	('president', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42'),
	('member', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42');

-- Application
INSERT INTO permission (name) VALUES ('role.crud'); 
INSERT INTO permission (name) VALUES ('role.add_permission'); 
INSERT INTO permission (name) VALUES ('news.crud'); 
INSERT INTO permission (name) VALUES ('permission.crud'); 
INSERT INTO permission (name) VALUES ('user_role.set'); 
INSERT INTO permission (name) VALUES ('user_role.update'); 
INSERT INTO permission (name) VALUES ('user.view_information'); -- Can view any account information
INSERT INTO permission (name) VALUES ('user.view_information_self'); -- Just see self information 
INSERT INTO permission (name) VALUES ('user.delete_account'); -- Can delete any account
INSERT INTO permission (name) VALUES ('user.profile.update'); -- Can update myself account

INSERT INTO role (name) VALUES ('President');
INSERT INTO role (name) VALUES ('Member');

INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member'),
		(SELECT id FROM role WHERE name = 'Member')
);


INSERT INTO role_permission (role_id, permission_id) VALUES (2,
	(SELECT id FROM permission WHERE name = 'user.profile.update' AND context = 'app')
);

-- Guild
INSERT INTO guild (code, name) VALUES ('M','Media');
INSERT INTO guild (code, name) VALUES ('T','Technical');
INSERT INTO guild (code, name) VALUES ('HR','Human Resources');
INSERT INTO guild (code, name) VALUES ('P','Plan');

INSERT INTO role (name) VALUES ('Technical_Leader');

INSERT INTO permission (context, name) VALUES ('guild', 'role.crud');
INSERT INTO role_permission (role_id, permission_id) VALUES (
	(SELECT id FROM role WHERE name = 'Technical_Leader'), 
	(SELECT id FROM permission WHERE name = 'role.crud' AND context = 'guild')
);
INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member'),
		(SELECT id FROM role WHERE name = 'Technical_Leader')
);

-- Crew
INSERT INTO crew (code, name) VALUES ('BE1', 'Backend 1');
INSERT INTO crew (code, name) VALUES ('BE2', 'Backend 2');
INSERT INTO crew (code, name) VALUES ('BE3', 'Backend 3');
INSERT INTO crew (code, name) VALUES ('BE4', 'Backend 4');

INSERT INTO permission (context, name) VALUES ('crew', 'view');
INSERT INTO permission (context, name) VALUES ('crew', 'role.crud');

INSERT INTO role (name) VALUES ('BE_Leader');
INSERT INTO role (name) VALUES ('BE_ViceConsultant');
INSERT INTO role (name) VALUES ('BE1_Mentor');
INSERT INTO role (name) VALUES ('BE1_Member');

INSERT INTO role_permission (role_id, permission_id) VALUES (
	(SELECT id FROM role WHERE name = 'BE1_Member'), 
	(SELECT id FROM permission WHERE name = 'role.crud' AND context = 'crew')
);
INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member'),
		(SELECT id FROM role WHERE name = 'BE1_Member')
);


INSERT INTO generation(id) VALUES (19);


