DROP TABLE IF EXISTS user_account;

CREATE TABLE user_account
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    github_user_id    BIGINT       NOT NULL,
    github_login      VARCHAR(100) NOT NULL,
    github_email      VARCHAR(150),
    github_avatar_url VARCHAR(255),
    github_name       VARCHAR(100),
    last_synced_at    TIMESTAMP,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);