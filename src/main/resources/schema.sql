drop table IF EXISTS mpa cascade;
drop table IF EXISTS films cascade;
drop table IF EXISTS genres cascade;
drop table IF EXISTS filmgenres cascade;
drop table IF EXISTS users cascade;
drop table IF EXISTS friends cascade;
drop table IF EXISTS likes cascade;

create table if not exists mpa(
id integer generated by default as identity not null primary key,
name varchar (200) not null unique
);

create table if not exists films (
id integer generated by default as identity not null primary key,
name varchar(200) not null,
description varchar(200) not null,
release_date date not null,
duration integer not null,
mpa_id integer not null references mpa(id) on delete restrict,
rate integer default 0
);

create table if not exists genres(
id integer generated by default as identity not null primary key,
name varchar (200) not null unique
);

create table if not exists genres_films (
id integer generated by default as identity not null primary key,
film_id integer not null references films(id) on delete cascade,
genre_id integer not null references genres(id) on delete restrict
);


create table if not exists users(
id integer generated by default as identity not null primary key,
login varchar(200) not null,
name varchar(200),
birthday date not null,
email varchar(200) not null
);

create table if not exists friends(
user_id integer references users(id) on delete cascade,
friend_id integer references users(id) on delete cascade,
primary key (user_id, friend_id)
);

create table if not exists likes(
film_id integer references films(id) on delete cascade,
user_id integer references users(id) on delete cascade,
primary key (film_id, user_id)
);