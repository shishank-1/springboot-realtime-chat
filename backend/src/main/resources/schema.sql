create table if not exists users (
    id bigserial primary key,
    username varchar(40) not null unique,
    email varchar(120) not null unique,
    password varchar(255) not null,
    profile_image varchar(255),
    role varchar(20) not null,
    created_at timestamp with time zone not null
);

create table if not exists chats (
    id bigserial primary key,
    created_at timestamp with time zone not null
);

create table if not exists chat_participants (
    id bigserial primary key,
    chat_id bigint not null references chats(id) on delete cascade,
    user_id bigint not null references users(id) on delete cascade,
    constraint uk_chat_user unique (chat_id, user_id)
);

create table if not exists messages (
    id bigserial primary key,
    chat_id bigint not null references chats(id) on delete cascade,
    sender_id bigint not null references users(id) on delete cascade,
    type varchar(20) not null,
    text text,
    media_url varchar(255),
    created_at timestamp with time zone not null
);

create index if not exists idx_messages_chat_created on messages(chat_id, created_at);
create index if not exists idx_users_username on users(username);
create index if not exists idx_users_email on users(email);
