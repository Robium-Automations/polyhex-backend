create table users
(
    user_id       varchar not null,
    bday          date,
    fname         varchar,
    lname         varchar,
    username      varchar not null,
    university_id varchar,
    study_program varchar,
    points        integer default 0,
    avatar        varchar
);

create unique index table_name_user_id_uindex
    on users (user_id);

create unique index table_name_username_uindex
    on users (username);
