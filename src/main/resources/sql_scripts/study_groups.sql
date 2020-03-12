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

