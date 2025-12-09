CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    room_id VARCHAR(255),
    sender_id BIGINT NOT NULL REFERENCES users(id),
    content TEXT,
    created_at TIMESTAMP
);
