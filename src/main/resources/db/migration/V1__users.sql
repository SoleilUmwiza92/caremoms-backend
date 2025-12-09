CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    supabase_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255),
    display_name VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
