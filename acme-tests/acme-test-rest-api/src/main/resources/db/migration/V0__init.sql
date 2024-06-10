create table api_user
(
    id bigint primary key generated always as identity,
    first_name text,
    last_name text,
    email text,
    address jsonb
);
