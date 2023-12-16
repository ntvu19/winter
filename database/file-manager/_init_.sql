create table if not exists storage_plan(
	plan_id			bigserial,
	plan_name		varchar(40) not null,
	full_size		bigserial not null,
	constraint PK_plan primary key(plan_id)
);

create table if not exists users(
	user_id			bigserial,
	full_name		varchar(40) not null,
	email			varchar(50) not null,
	updated_at		timestamptz default now(),
	is_blocked		boolean default false not null,
	is_deleted		boolean default false not null,
	plan_id			bigserial not null,
	constraint PK_user primary key(user_id),
	constraint FK_user_plan foreign key(plan_id)
	references storage_plan(plan_id)
);

create table if not exists categories(
	category_id		bigserial,
	"name"			varchar(30),
	description		text,
	constraint PK_category primary key(category_id)
);

create table if not exists objects(
	object_id		bigserial,
	owner_id		bigserial not null,
	"name"			varchar(256) not null,
	description		text,
	shared_url		text not null,
	parent_id		bigserial,
	is_secure		boolean default false not null,
	is_public		boolean default false not null,
	created_at		timestamptz default now(),
	updated_at		timestamptz default now(),
	created_by		bigserial not null,
	updated_by		bigserial not null,
	constraint PK_object primary key(object_id),
	constraint FK_object_user_owner foreign key(owner_id)
	references users(user_id) on delete cascade,
	constraint FK_object_object_parent foreign key(parent_id)
	references objects(object_id) on delete cascade,
	constraint FK_object_user_created foreign key(created_by)
	references users(user_id),
	constraint FK_object_user_updated foreign key(updated_by)
	references users(user_id)
);

create table if not exists permissions(
	user_id			bigserial not null,
	object_id		bigserial not null,
	"read"			boolean default true not null,
	"write"			boolean default true not null,
	constraint PK_permission primary key(user_id, object_id),
	constraint FK_permission_user foreign key(user_id)
	references users(user_id) on delete cascade,
	constraint FK_permission_object foreign key(object_id)
	references objects(object_id) on delete cascade
);

create table if not exists secure_objects(
	object_id		bigserial not null,
	hash_secure_key	varchar(500) not null,
	salt			varchar(10) not null,
	constraint FK_secure_object foreign key(object_id)
	references objects(object_id) on delete cascade
);

create table if not exists folders(
	object_id		bigserial not null,
	constraint FK_folder_object foreign key(object_id)
	references objects(object_id) on delete cascade
);

create table if not exists files(
	object_id		bigserial not null,
	"extension"		varchar(10),
	category_id		bigserial not null,
	"size"			bigserial not null,
	constraint FK_file_object foreign key(object_id)
	references objects(object_id) on delete cascade,
	constraint FK_file_category foreign key(category_id)
	references categories(category_id)
);
