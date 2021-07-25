CREATE TABLE person (
    id SERIAL PRIMARY KEY NOT NULL,
    login VARCHAR(2000),
    password VARCHAR(2000)
);

INSERT INTO person (login, password) values ('Petr', '123');
INSERT INTO person (login, password) values ('Ivan', '123');
INSERT INTO person (login, password) values ('Ban', '123');