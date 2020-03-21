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

