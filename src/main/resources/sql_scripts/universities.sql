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
