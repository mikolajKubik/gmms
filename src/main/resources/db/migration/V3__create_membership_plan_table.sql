CREATE TABLE membership_plan
(
    id              UUID          NOT NULL,
    version         BIGINT        NOT NULL,
    gym_id          UUID          NOT NULL,
    plan_type_id    BIGINT        NOT NULL,
    name            VARCHAR(255)  NOT NULL,
    price           DECIMAL(10,2) NOT NULL,
    currency        VARCHAR(3)    NOT NULL,
    duration_months INT           NOT NULL,
    max_members     INT           NOT NULL,
    CONSTRAINT pk_membership_plan PRIMARY KEY (id),
    CONSTRAINT fk_membership_plan_gym FOREIGN KEY (gym_id) REFERENCES gym (id),
    CONSTRAINT fk_membership_plan_type FOREIGN KEY (plan_type_id) REFERENCES plan_type (id)
);

