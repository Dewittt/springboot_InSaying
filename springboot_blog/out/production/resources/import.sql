insert into user (id,username,password,email) values (1,'dewitt','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','dewittt@163.com');
insert into user (id,username,password,email) values (2,'mike','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','dewitt@163.com');

insert into authority (id,name) values (1,'ROLE_ADMIN');
insert into authority (id,name) values (2,'ROLE_USER');

insert into user_authority (user_id,authorities_id) values (1,1);
insert into user_authority (user_id,authorities_id) values (2,2);
