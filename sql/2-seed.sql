INSERT INTO user_account (username, hashed_password) VALUES 
	('president', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42'),
	('member', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42'),
	('member1', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42'),
	('member2', '$2a$04$JPF2tFVm7Ag1mRiSA36dzufKFtEjodBKixhtkm6bifpKPWd5QTS42');

INSERT INTO generation(id) VALUES (19);

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
INSERT INTO role (is_default, name) VALUES (true, 'Member');

INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member'),
		(SELECT id FROM role WHERE name = 'Member')
);
INSERT INTO user_profile (account_id, full_name, student_code, email, generation_id, dob)
VALUES 
	((SELECT id FROM user_account WHERE username = 'member'), 'member', 'SE000000', 'test@gmail.com', (SELECT MAX(id) FROM generation), (SELECT CURRENT_TIMESTAMP)),
	((SELECT id FROM user_account WHERE username = 'member1'), 'member1', 'SE000001', 'test1@gmail.com', (SELECT MAX(id) FROM generation), (SELECT CURRENT_TIMESTAMP)),
	((SELECT id FROM user_account WHERE username = 'member2'), 'member2', 'SE000002', 'test2@gmail.com', (SELECT MAX(id) FROM generation), (SELECT CURRENT_TIMESTAMP));


INSERT INTO role_permission (role_id, permission_id) VALUES (2,
	(SELECT id FROM permission WHERE name = 'user.profile.update' AND context = 'app')
);

-- Guild
INSERT INTO guild (code, name) VALUES ('M','Media');
INSERT INTO guild (code, name) VALUES ('T','Technical');
INSERT INTO guild (code, name) VALUES ('HR','HumanResources');
INSERT INTO guild (code, name) VALUES ('P','Plan');

INSERT INTO role (name) VALUES ('T_Leader');
INSERT INTO role (is_default, name) VALUES (true, 'T_Member');

INSERT INTO permission (context, name) VALUES ('guild', 'delete');
INSERT INTO permission (context, name) VALUES ('guild', 'update');
INSERT INTO permission (context, name) VALUES ('guild', 'role.crud');
INSERT INTO permission (context, name) VALUES ('guild', 'member.crud');

INSERT INTO role_permission (role_id, permission_id) VALUES (
	(SELECT id FROM role WHERE name = 'T_Leader'), 
	(SELECT id FROM permission WHERE name = 'role.crud' AND context = 'guild')
), (
	(SELECT id FROM role WHERE name = 'T_Leader'), 
	(SELECT id FROM permission WHERE name = 'member.crud' AND context = 'guild')
);
INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member'),
		(SELECT id FROM role WHERE name = 'T_Leader')
);
INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member1'),
		(SELECT id FROM role WHERE name = 'T_Leader')
);
INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member2'),
		(SELECT id FROM role WHERE name = 'T_Leader')
);

-- Crew
INSERT INTO crew (code, name) VALUES ('BE1', 'Backend1');
INSERT INTO crew (code, name) VALUES ('BE2', 'Backend2');
INSERT INTO crew (code, name) VALUES ('BE3', 'Backend3');
INSERT INTO crew (code, name) VALUES ('BE4', 'Backend4');

INSERT INTO permission (context, name) VALUES ('crew', 'view');
INSERT INTO permission (context, name) VALUES ('crew', 'role.crud');

INSERT INTO role (name) VALUES ('BE_Leader');
INSERT INTO role (name) VALUES ('BE_ViceConsultant');
INSERT INTO role (name) VALUES ('BE1_Mentor');
INSERT INTO role (is_default, name) VALUES (true, 'BE1_Member');

INSERT INTO role_permission (role_id, permission_id) VALUES (
	(SELECT id FROM role WHERE name = 'BE1_Member'), 
	(SELECT id FROM permission WHERE name = 'role.crud' AND context = 'crew')
);
INSERT INTO user_role (account_id, role_id) VALUES (
		(SELECT id FROM user_account WHERE username = 'member'),
		(SELECT id FROM role WHERE name = 'BE1_Member')
);



