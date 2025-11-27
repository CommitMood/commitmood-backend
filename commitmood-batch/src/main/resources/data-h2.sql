-- CommitMood Initial Data
-- Spring Boot will execute this file after schema.sql

-- 테스트용 사용자 데이터
INSERT INTO user_account (
    GITHUB_USER_ID,
    GITHUB_LOGIN,
    GITHUB_EMAIL,
    GITHUB_AVATAR_URL,
    GITHUB_NAME
)
VALUES
    ('52474291', 'swkim12345', 'swkim12345@naver.com', 'https://avatars.githubusercontent.com/u/52474291?v=4', 'sunghwki');
