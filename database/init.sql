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
        '080d5db6-f8ea-48c8-9dd7-f4e1462a28e1'),
       ('5f135323-323b-4982-95b4-a7d2a3be5dee', 'DDS', 'Design of distributed systems',
        '080d5db6-f8ea-48c8-9dd7-f4e1462a28e1'),
       ('d51b1f3c-1940-44d4-95d5-bea848d43e8a', 'CTISE', 'Current trends in software engineering',
        '080d5db6-f8ea-48c8-9dd7-f4e1462a28e1');

-- 5. Create study group
insert into study_groups
values ('b9723ce3-9341-4f61-9c5f-e4f5faa1886c', 'XML: WS 2019/2020', '2c7fb28f-8c23-43f5-8db8-f37db1780720',
        '014d8b49-be51-4cc6-82c2-841ecb8e6fca'),
       ('231155bd-b522-4b53-90ba-f5091feae24b', 'SSE: WS 2019/2020', 'f9062c8d-5c0b-4451-b7f5-0f7177a228a4',
        '014d8b49-be51-4cc6-82c2-841ecb8e6fca'),
       ('4018910d-5395-4c92-aacb-3e4922d92369', 'DDS: WS 2019/2020', '5f135323-323b-4982-95b4-a7d2a3be5dee',
        '014d8b49-be51-4cc6-82c2-841ecb8e6fca'),
       ('2f871677-2046-47e0-83ba-d99e2a2033dd', 'Current trends: WS 2019/2020', 'd51b1f3c-1940-44d4-95d5-bea848d43e8a',
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
values ('d99e04c9-d924-46d0-895a-49a2b73b3abf', '1964-1-12', 'Jeff', 'Bezos', 'jeff.bezos',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user3
insert into user_credentials
values ('8887c838-75b0-4ed7-84e0-2a2686f970ec', 'elon.musk', 'elon.musk@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('8887c838-75b0-4ed7-84e0-2a2686f970ec', '1971-6-28', 'Elon', 'Musk', 'elon.musk',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user4 Waldemar
insert into user_credentials
values ('bf2aaf9c-1929-4451-9b6f-aeaa8124ed65', 'waldemar.firus', 'waldemar.firus@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('bf2aaf9c-1929-4451-9b6f-aeaa8124ed65', '1988-10-29', 'Waldemar', 'Firus', 'waldemar.firus',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user5 Alex
insert into user_credentials
values ('18dd51f4-643d-4207-a441-d21e75ee98fe', 'alex.senger', 'alex.senger@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('18dd51f4-643d-4207-a441-d21e75ee98fe', '1994-3-28', 'Alex', 'Senger', 'alex.senger',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user6 Taras
insert into user_credentials
values ('cd7616c2-dc87-457e-86da-2f4dff56b955', 'taras.lavreniuk', 'taras.lavreniuk@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('cd7616c2-dc87-457e-86da-2f4dff56b955', '1997-3-22', 'Taras', 'Lavreniuk', 'taras.lavreniuk',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user7 Gurami
insert into user_credentials
values ('3bccd6fa-f61e-49e4-837a-b36fdd6e28c4', 'guga.khatsiashvili', 'guga.khatsiashvili@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('3bccd6fa-f61e-49e4-837a-b36fdd6e28c4', '1996-2-29', 'Gurami', 'Khatsiashvili', 'guga.khatsiashvili',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);

-- user8 Jen
insert into user_credentials
values ('771f3091-3cd8-441d-85a0-1ef1c285963c', 'tsungjen.pu', 'tsungjen.pu@tuchemnitz.de',
        '4ea055d960389ac53511b68bc96231ad',
        'user'); -- password: password
insert into users
values ('771f3091-3cd8-441d-85a0-1ef1c285963c', '1990-6-13', 'Jen', 'Pu', 'tsungjen.pu',
        '398adbad-5b94-4a19-9fc8-d130e4614844', 'Web engineering', 0, null);


-- 7. Add users to a group
insert into users_groups
values ('aa490296-7707-4add-a3db-353594ced72b', '4242d5b2-e82d-4ebd-b5c4-a6fa66970048',
        '231155bd-b522-4b53-90ba-f5091feae24b'),
       ('ebbc6d76-bc46-4af4-8e23-744592e50b99', 'd99e04c9-d924-46d0-895a-49a2b73b3abf',
        '231155bd-b522-4b53-90ba-f5091feae24b'),
       ('5ccabe02-9623-4b50-bf3b-33cdcde1410c', '8887c838-75b0-4ed7-84e0-2a2686f970ec',
        '231155bd-b522-4b53-90ba-f5091feae24b');

-- Add users to DDS group
insert into users_groups
values ('eed98f41-db04-4496-8032-da538cf927ea', 'bf2aaf9c-1929-4451-9b6f-aeaa8124ed65',
        '4018910d-5395-4c92-aacb-3e4922d92369'),
       ('5cdcbcac-c237-420f-b3d4-c59d7b3de684', '18dd51f4-643d-4207-a441-d21e75ee98fe',
        '4018910d-5395-4c92-aacb-3e4922d92369'),
       ('1cc372ee-720f-47c7-ab10-d76bc7dc35e7', 'cd7616c2-dc87-457e-86da-2f4dff56b955',
        '4018910d-5395-4c92-aacb-3e4922d92369'),
       ('195d0c86-25e0-4fd3-964f-2690757aa491', '3bccd6fa-f61e-49e4-837a-b36fdd6e28c4',
        '4018910d-5395-4c92-aacb-3e4922d92369'),
       ('f6f89804-12be-4ad9-b08d-54695caad2ea', '771f3091-3cd8-441d-85a0-1ef1c285963c',
        '4018910d-5395-4c92-aacb-3e4922d92369');






