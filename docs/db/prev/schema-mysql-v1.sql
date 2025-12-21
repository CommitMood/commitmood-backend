-- 1) 유저 (GitHub 계정 기준)
CREATE TABLE user_account
(
    user_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(50)  NOT NULL UNIQUE,  -- 서비스 로그인 ID
    email               VARCHAR(100) NOT NULL UNIQUE,  -- 이메일
    password            VARCHAR(255) NOT NULL,         -- 암호화된 비밀번호

    github_id           VARCHAR(100) UNIQUE,           -- GitHub 로그인 ID (선택)
    github_login        VARCHAR(100) UNIQUE,           -- GitHub username
    github_email        VARCHAR(150) NULL,             -- GitHub 이메일(없을 수도 있음)
    github_avatar_url   VARCHAR(255) NULL,             -- GitHub 프로필 사진

    is_github_connected TINYINT(1) NOT NULL DEFAULT 0, -- 연동 여부

    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


-- 2) 레포지토리 (분석 대상)
CREATE TABLE repository
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id       BIGINT       NOT NULL, -- app_user.id
    github_repo_id BIGINT       NOT NULL, -- GitHub repo id

    name           VARCHAR(100) NOT NULL, -- 단일 이름
    full_name      VARCHAR(200) NOT NULL, -- owner/name
    default_branch VARCHAR(100) NULL,

    html_url       VARCHAR(255) NULL,
    is_private     TINYINT(1)   NOT NULL DEFAULT 0,

    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_synced_at DATETIME NULL,

    CONSTRAINT fk_repository_owner
        FOREIGN KEY (owner_id) REFERENCES app_user (id),

    UNIQUE KEY uk_repository_github_repo_id (github_repo_id),
    KEY            idx_repository_owner (owner_id)
);


-- 3) 커밋 원본 로그
CREATE TABLE commit_log
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    repo_id           BIGINT   NOT NULL, -- repository.id
    author_id         BIGINT   NOT NULL, -- app_user.id (커밋 작성자)
    github_commit_sha CHAR(40) NOT NULL, -- SHA-1

    committed_at      DATETIME NOT NULL, -- 커밋 시간(UTC or KST, 일관되게)
    message           TEXT     NOT NULL,
    html_url          VARCHAR(255) NULL,

    additions         INT NULL,
    deletions         INT NULL,
    total_changes     INT NULL,

    created_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_commit_log_repo
        FOREIGN KEY (repo_id) REFERENCES repository (id),

    CONSTRAINT fk_commit_log_author
        FOREIGN KEY (author_id) REFERENCES app_user (id),

    -- 같은 repo 내에서 SHA는 유니크
    UNIQUE KEY uk_commit_log_repo_sha (repo_id, github_commit_sha),

    -- 랭킹/통계를 위해 자주 쓸 인덱스
    KEY               idx_commit_log_author_committed_at (author_id, committed_at),
    KEY               idx_commit_log_repo_committed_at (repo_id, committed_at)
);


-- 4) 커밋 분석 결과
CREATE TABLE commit_analysis
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    commit_id       BIGINT   NOT NULL,           -- commit_log.id

    flagged_count   INT      NOT NULL DEFAULT 0, -- 문제 단어 총합
    swear_count     INT      NOT NULL DEFAULT 0, -- 욕설 개수
    exclaim_count   INT      NOT NULL DEFAULT 0, -- 감탄/과한 표현 개수
    emoji_count     INT      NOT NULL DEFAULT 0, -- 감정 Emoji 개수

    sentiment       VARCHAR(20) NULL,            -- 'positive','neutral','negative' 등
    sentiment_score DECIMAL(5, 2) NULL,          -- -1.00 ~ 1.00 등 규칙 정의

    analyzed_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_commit_analysis_commit
        FOREIGN KEY (commit_id) REFERENCES commit_log (id),

    UNIQUE KEY uk_commit_analysis_commit (commit_id)
);


-- 5) 커밋별 플래그 단어/표현
CREATE TABLE flagged_token
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    commit_id  BIGINT       NOT NULL,           -- commit_log.id

    token      VARCHAR(100) NOT NULL,           -- 실제 단어/표현
    token_type ENUM('SWEAR','SLANG','EMOJI','EMPHASIS','OTHER')
                NOT NULL DEFAULT 'OTHER',
    weight     INT          NOT NULL DEFAULT 1, -- 가중치(랭킹 계산에 활용 가능)

    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_flagged_token_commit
        FOREIGN KEY (commit_id) REFERENCES commit_log (id),

    KEY        idx_flagged_token_commit (commit_id),
    KEY        idx_flagged_token_type_token (token_type, token)
);