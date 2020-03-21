create table shared_materials
(
	shared_material_id varchar not null,
	material_id varchar not null,
	group_id varchar
);

create unique index shared_materials_shared_material_id_uindex
	on shared_materials (shared_material_id);

alter table shared_materials
	add constraint shared_materials_pk
		primary key (shared_material_id);

