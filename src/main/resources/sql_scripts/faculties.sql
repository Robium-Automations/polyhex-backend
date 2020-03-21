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

