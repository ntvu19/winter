-- IsActive field
alter table users add column "is_active" boolean default false;

-- Birthday field
alter table users add column "birthday" date not null;
