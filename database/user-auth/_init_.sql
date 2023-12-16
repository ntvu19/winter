create table if not exists storage_plan(
	plan_id			bigserial,
	plan_name		varchar(40) not null,
	full_size		bigserial not null,
	price			numeric(8,5) not null,
	currency		varchar(10) not null,
	details			json,
	"type"			varchar(15) not null,
	constraint PK_plan primary key(plan_id)
);

create table if not exists users(
	user_id			bigserial,
	full_name		varchar(40) not null,
	email			varchar(50) not null,
	"password"		varchar(500) not null,
	salt			varchar(10) not null,
	phone			varchar(15) not null,
	avatar_url		varchar(500),
	is_blocked		boolean default false not null,
	is_deleted		boolean	default false not null,
	created_at		timestamptz default now(),
	updated_at		timestamptz default now(),
	plan_id			bigserial not null,
	constraint PK_user primary key(user_id),
	constraint FK_users_plan foreign key(plan_id)
	references 	storage_plan(plan_id)
);

create table if not exists otp(
	otp_id			bigserial,
	code			varchar(10) not null,
	user_id			bigserial not null,
	"type"			varchar(15) not null,
	remain_time		smallint not null,
	expired_at		timestamptz default now(),
	constraint PK_otp primary key(otp_id),
	constraint FK_otp_user foreign key(user_id)
	references users(user_id) on delete cascade
);

create table if not exists sessions(
	session_id		bigserial,
	user_id			bigserial not null,
	device_id		varchar(50) not null,
	device_name		varchar(50) not null,
	access_token	varchar(500) not null,
	refresh_token	varchar(500) not null,
	last_login		timestamptz default now(),
	constraint PK_session primary key(session_id),
	constraint FK_session_user foreign key(user_id)
	references users(user_id) on delete cascade
);
