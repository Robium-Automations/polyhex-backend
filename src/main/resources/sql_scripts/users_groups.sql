create table users_groups
(
    id varchar not null,
    user_id varchar not null,
    study_group_id varchar not null
);

create unique index users_groups_id_uindex
    on users_groups (id);

alter table users_groups
    add constraint users_groups_pk
        primary key (id);

