drop database if exists company;
create database company;
use company;

create table employees (
    id int auto_increment not null,
    first_name varchar(64) not null,
    last_name varchar(64) not null,
    email varchar(125) not null,
    profile_url varchar(200) not null,
    primary key (id)
)


