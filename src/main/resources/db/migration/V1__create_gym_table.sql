CREATE TABLE gym
(
    id           UUID         NOT NULL,
    version      BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL UNIQUE,
    address      VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50)  NOT NULL,
    CONSTRAINT pk_gym PRIMARY KEY (id)
);

INSERT INTO gym (id, version, name, address, phone_number) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 0, 'FitLife Center', 'ul. Przykładowa 1, Warszawa', '+48123456789'),
    ('550e8400-e29b-41d4-a716-446655440002', 0, 'PowerGym Kraków', 'ul. Dietla 42, Kraków', '+48987654321'),
    ('550e8400-e29b-41d4-a716-446655440003', 0, 'AquaFit Gdańsk', 'ul. Długa 33, Gdańsk', '+48555123456');

