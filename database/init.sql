-- CREATE DB AND CONNECT TO IT
-- CREATE DATABASE polyhex_db;

\c polyhex_db;

-- CREATE DATABASE TABLES

-- UNIVERSITIES
create table universities
(
    university_id   varchar not null,
    university_name varchar not null
);

create unique index universities_university_id_uindex
    on universities (university_id);

create unique index universities_university_name_uindex
    on universities (university_name);

alter table universities
    add constraint universities_pk
        primary key (university_id);

-- SEMESTERS
create table semesters
(
    semester_id          varchar not null,
    semester_name        varchar not null,
    semester_description varchar,
    start_date           date,
    end_date             date,
    university_id        varchar not null
);

create unique index semesters_semester_uindex
    on semesters (semester_id);

alter table semesters
    add constraint semesters_pk
        primary key (semester_id);

-- FACULTIES
create table faculties
(
    faculty_id    varchar not null,
    faculty_name  varchar not null,
    university_id varchar not null
);

create unique index faculties_faculty_id_uindex
    on faculties (faculty_id);

alter table faculties
    add constraint faculties_pk
        primary key (faculty_id);

-- SUBJECTS
create table subjects
(
    subject_id          varchar not null,
    subject_name        varchar not null,
    subject_description varchar,
    faculty_id          varchar not null
);

create unique index subjects_subject_id_uindex
    on subjects (subject_id);

alter table subjects
    add constraint subjects_pk
        primary key (subject_id);

-- STUDY_GROUPS
create table study_groups
(
    study_group_id   varchar not null,
    study_group_name varchar not null,
    subject_id       varchar not null,
    semester_id      varchar not null
);

create unique index study_groups_study_group_id_uindex
    on study_groups (study_group_id);

alter table study_groups
    add constraint study_groups_pk
        primary key (study_group_id);

-- MATERIALS
create table materials
(
    material_id   VARCHAR not null,
    path          varchar not null,
    name          varchar not null,
    data_type     varchar not null,
    author_id     varchar not null,
    creating_date varchar not null
);

create unique index materials_material_id_uindex
    on materials (material_id);

alter table materials
    add constraint materials_pk
        primary key (material_id);

-- SHARED_MATERIALS
create table shared_materials
(
    shared_material_id varchar not null,
    material_id        varchar not null,
    group_id           varchar
);

create unique index shared_materials_shared_material_id_uindex
    on shared_materials (shared_material_id);

alter table shared_materials
    add constraint shared_materials_pk
        primary key (shared_material_id);

-- USER_CREDENTIALS
create table user_credentials
(
    user_id   varchar not null,
    username  varchar not null,
    email     varchar,
    password  varchar not null,
    user_role varchar not null
);

create unique index user_credentials_user_id_uindex
    on user_credentials (user_id);

create unique index user_credentials_username_uindex
    on user_credentials (username);

create unique index user_credentials_email_uindex
    on user_credentials (email);

alter table user_credentials
    add constraint user_credentials_pk
        primary key (user_id);

-- USERS
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

-- USERS_GROUPS
create table users_groups
(
    id             varchar not null,
    user_id        varchar not null,
    study_group_id varchar not null
);

create unique index users_groups_id_uindex
    on users_groups (id);

alter table users_groups
    add constraint users_groups_pk
        primary key (id);



-- PREPOPULATE DATABASE

-- 1. Create university
insert into universities
values ('398adbad-5b94-4a19-9fc8-d130e4614844', 'TU Chemnitz');

-- 2. Create semesters
insert into semesters
values ('014d8b49-be51-4cc6-82c2-841ecb8e6fca', 'WS 2019/2020', 'Winter semester 2019/2020', '2019-10-1',
        '2020-3-31', '398adbad-5b94-4a19-9fc8-d130e4614844');

-- 3. Create faculty
insert into faculties
values ('080d5db6-f8ea-48c8-9dd7-f4e1462a28e1', 'Faculty of Computer Science', '398adbad-5b94-4a19-9fc8-d130e4614844');

-- 4. Create subjects
insert into subjects
values ('2c7fb28f-8c23-43f5-8db8-f37db1780720', 'XML', 'Extensible Markup Language',
        '080d5db6-f8ea-48c8-9dd7-f4e1462a28e1'),
       ('f9062c8d-5c0b-4451-b7f5-0f7177a228a4', 'SSE', 'Software service engineering',
        '080d5db6-f8ea-48c8-9dd7-f4e1462a28e1');

-- 5. Create study group
insert into study_groups
values ('231155bd-b522-4b53-90ba-f5091feae24b', 'SSE: WS 2019/2020', 'f9062c8d-5c0b-4451-b7f5-0f7177a228a4',
        '014d8b49-be51-4cc6-82c2-841ecb8e6fca');

-- 6. Create few users
-- user1
insert into user_credentials
values ('4242d5b2-e82d-4ebd-b5c4-a6fa66970048', 'admin', 'admin@tuchemnitz.de', '3ade61461f1d14818dee104275907b1a',
        'moderator'); -- password: admin
insert into users
values ('4242d5b2-e82d-4ebd-b5c4-a6fa66970048', '1994-10-29', 'Admin', 'Admin', 'admin',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user2
insert into user_credentials
values ('d99e04c9-d924-46d0-895a-49a2b73b3abf', 'jeff.bezos', 'jeff.bezos@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('d99e04c9-d924-46d0-895a-49a2b73b3abf', '1964-1-12', 'Jeff', 'Bezoz', 'jeff.bezos',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user3
insert into user_credentials
values ('8887c838-75b0-4ed7-84e0-2a2686f970ec', 'elon.musk', 'elon.musk@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('8887c838-75b0-4ed7-84e0-2a2686f970ec', '1971-6-28', 'Elon', 'Musk', 'elon.musk',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- 7. Add users to a group
insert into users_groups
values ('aa490296-7707-4add-a3db-353594ced72b', '4242d5b2-e82d-4ebd-b5c4-a6fa66970048',
        '231155bd-b522-4b53-90ba-f5091feae24b'),
       ('ebbc6d76-bc46-4af4-8e23-744592e50b99', 'd99e04c9-d924-46d0-895a-49a2b73b3abf',
        '231155bd-b522-4b53-90ba-f5091feae24b'),
       ('5ccabe02-9623-4b50-bf3b-33cdcde1410c', '8887c838-75b0-4ed7-84e0-2a2686f970ec',
        '231155bd-b522-4b53-90ba-f5091feae24b');






