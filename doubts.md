#### add "users" schema
```postgresql
create table users (
    user_id serial primary key,
    email varchar(250) unique not null,
    password varchar(250) not null,
    role varchar(50) not null
);

insert into users (email, password, role) values ('abc@mail.com', '{noop}DummyUser875', 'read');
insert into users (email, password, role) values ('admin@mail.com', '{bcrypt}$2a$12$AUi8iLlohkNutrQBSCY2wOTLdaqajC9BzAqIU7541cE5BmXrM0sXq', 'admin');
```
