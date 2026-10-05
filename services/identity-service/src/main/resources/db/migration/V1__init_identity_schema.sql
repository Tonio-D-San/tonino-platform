CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE groups (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    updated_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    role VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT uk_groups_role UNIQUE (role)
);

CREATE TABLE users (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    updated_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    group_id UUID NOT NULL,
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT fk_users_group FOREIGN KEY (group_id) REFERENCES groups (uuid)
);

CREATE INDEX idx_users_group_id ON users (group_id);
CREATE INDEX idx_users_is_active ON users (is_active);
CREATE INDEX idx_groups_is_active ON groups (is_active);
CREATE INDEX idx_groups_path ON groups (path);

CREATE OR REPLACE FUNCTION set_updated_at_epoch_ms()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_groups_set_updated_at
BEFORE UPDATE ON groups
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_epoch_ms();

CREATE TRIGGER trg_users_set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_epoch_ms();
