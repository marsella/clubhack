drop table if exists clubs;

create table clubs (
  id integer primary key autoincrement,
  name string not null,
  password string not null
--  description string,
--  calID string
--  members string
);

--create table members (
--  id integer primary key autoincrement,
 -- name string not null,
  --email string not null
--);

