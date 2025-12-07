CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE known_places (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),            -- "Home", "Starbucks North Ave", "Office"
    category VARCHAR(50),         -- "Residential", "Cafe", "Work"
    loc GEOMETRY(POINT, 4326),   
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE visits (
    id BIGSERIAL PRIMARY KEY,
    place_id BIGINT REFERENCES known_places(id),
    entry_time TIMESTAMPTZ NOT NULL,
    exit_time TIMESTAMPTZ NOT NULL
);

CREATE TABLE location_logs (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMPTZ NOT NULL,
    device_id VARCHAR(50) NOT NULL,
    loc GEOMETRY(POINT, 4326),
    visit_id BIGINT REFERENCES visits(id)
);

CREATE TABLE transaction_logs (
    id BIGSERIAL PRIMARY KEY,
    extern_txn_id VARCHAR(255) UNIQUE NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    amount DECIMAL(10, 2),
    category VARCHAR(100),
    visit_id BIGINT REFERENCES visits(id)
);

CREATE TABLE health_logs (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMPTZ NOT NULL,
    metric_type VARCHAR(50) NOT NULL,
    val DOUBLE PRECISION NOT NULL,
    unit VARCHAR(20) NOT NULL,
    visit_id BIGINT REFERENCES visit(id)
);

CREATE TABLE dev_logs (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMPTZ NOT NULL,
    platform VARCHAR(50) NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    target VARCHAR(255) NOT NULL,
    metadata JSONB,
    visit_id BIGINT REFERENCES visits(id)
);
