create table semesters
(
    semester             varchar not null,
    semester_name        varchar not null,
    semester_description varchar,
    start_date           date,
    end_date             date
);

create unique index semesters_semester_uindex
    on semesters (semester);

alter table semesters
    add constraint semesters_pk
        primary key (semester);

