create table users
(
    user_id       varchar not null,
    username      varchar not null,
    fname         varchar,
    lname         varchar,
    bday          date,
    university_id varchar
);

create unique index table_name_user_id_uindex
    on users (user_id);

create unique index table_name_username_uindex
    on users (username);

