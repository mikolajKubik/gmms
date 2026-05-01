CREATE TABLE member
(
    id                  UUID         NOT NULL,
    version             BIGINT       NOT NULL,
    membership_plan_id  UUID         NOT NULL,
    full_name           VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    start_date          DATE         NOT NULL,
    status              VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_member PRIMARY KEY (id),
    CONSTRAINT fk_member_membership_plan FOREIGN KEY (membership_plan_id) REFERENCES membership_plan (id),
    CONSTRAINT member_status_check CHECK (status IN ('ACTIVE', 'CANCELLED'))
);

