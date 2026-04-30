CREATE TABLE users
(
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);
