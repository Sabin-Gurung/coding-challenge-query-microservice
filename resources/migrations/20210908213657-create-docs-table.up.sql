create table documents (
id int not null auto_increment primary key,
title varchar(255),
content varchar(2047),
created_at timestamp default now(),
updated_at timestamp default now(),
username varchar(255),
foreign key (username) references users(username)
);