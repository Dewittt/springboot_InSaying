insert into user (id,username,password,email) values (1,'dewitt','dewitt','dewittt@163.com');
insert into user (id,username,password,email) values (2,'mike','123456','dewitt@163.com');

insert into authority (id,name) values (1,'ROLE_ADMIN');
insert into authority (id,name) values (2,'ROLE_USER');

insert into user_authority (user_id,authorities_id) values (1,1);
insert into user_authority (user_id,authorities_id) values (2,2);
