CREATE TABLE seller_requests (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    documents_attached BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_seller_request_user FOREIGN KEY (user_id) REFERENCES users(id)
);