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