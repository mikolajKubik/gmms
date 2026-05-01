CREATE TABLE gym
(
    id           UUID         NOT NULL,
    version      BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL UNIQUE,
    address      VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50)  NOT NULL,
--     active       BOOLEAN      NOT NULL DEFAULT true,
    CONSTRAINT pk_gym PRIMARY KEY (id)
);
