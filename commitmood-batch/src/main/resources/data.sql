-- CommitMood Initial Data
-- Spring Boot will execute this file after schema.sql

-- 테스트용 사용자 데이터
INSERT INTO user_account (github_id, github_login, github_email, github_avatar_url, github_name)
VALUES
    ('52474291', 'swkim12345', 'swkim12345@naver.com', 'https://avatars.githubusercontent.com/u/52474291?v=4', 'sunghwki'),
ON DUPLICATE KEY UPDATE github_login = github_login;
