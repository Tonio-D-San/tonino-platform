-- Notes:
-- - created_at and updated_at are stored as epoch milliseconds, matching the Java Long fields.
-- - uuid uses gen_random_uuid(), provided by pgcrypto.
-- - The Java application can still provide UUID.randomUUID(); this DB default is useful for direct SQL inserts.
-- - updated_at is automatically refreshed through a trigger, mirroring @PreUpdate.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================
-- Table: groups
-- ============================================================

CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL PRIMARY KEY,

    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    updated_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    role VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    description TEXT,

    CONSTRAINT uk_groups_uuid UNIQUE (uuid),
    CONSTRAINT uk_groups_role UNIQUE (role)
);

-- ============================================================
-- Table: users
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,

    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    updated_at BIGINT NOT NULL DEFAULT (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    group_id BIGINT NOT NULL,

    CONSTRAINT uk_users_uuid UNIQUE (uuid),
    CONSTRAINT uk_users_email UNIQUE (email),

    CONSTRAINT fk_users_group
        FOREIGN KEY (group_id)
        REFERENCES groups (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- Indexes
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_users_group_id
    ON users (group_id);

CREATE INDEX IF NOT EXISTS idx_users_is_active
    ON users (is_active);

CREATE INDEX IF NOT EXISTS idx_groups_is_active
    ON groups (is_active);

CREATE INDEX IF NOT EXISTS idx_groups_path
    ON groups (path);

-- ============================================================
-- updated_at trigger
-- ============================================================

CREATE OR REPLACE FUNCTION set_updated_at_epoch_ms()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := (floor(extract(epoch from clock_timestamp()) * 1000))::BIGINT;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_groups_set_updated_at ON groups;

CREATE TRIGGER trg_groups_set_updated_at
BEFORE UPDATE ON groups
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_epoch_ms();

DROP TRIGGER IF EXISTS trg_users_set_updated_at ON users;

CREATE TRIGGER trg_users_set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_epoch_ms();
