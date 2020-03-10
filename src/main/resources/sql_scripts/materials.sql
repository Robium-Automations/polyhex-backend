create table materials
(
    material_id VARCHAR not null,
    path varchar not null,
    name varchar not null,
    data_type varchar not null,
    author_id varchar not null,
    creating_date varchar not null
);

create unique index materials_material_id_uindex
    on materials (material_id);

alter table materials
    add constraint materials_pk
        primary key (material_id);

