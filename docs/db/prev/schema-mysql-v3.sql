-- =========================
-- USER_ACCOUNT
-- =========================
CREATE TABLE USER_ACCOUNT
(
    USER_ID           BIGINT AUTO_INCREMENT PRIMARY KEY,                               -- PRIMARY KEY
    GITHUB_ID         BIGINT       NOT NULL UNIQUE,                                    -- GITHUB USER ID
    GITHUB_LOGIN      VARCHAR(100) NOT NULL UNIQUE,                                    -- GITHUB LOGIN
    GITHUB_EMAIL      VARCHAR(150) NULL,                                               -- GITHUB EMAIL
    GITHUB_AVATAR_URL VARCHAR(255) NULL,                                               -- GITHUB PROFILE IMAGE
    GITHUB_NAME       VARCHAR(100) NULL,                                               -- DISPLAY NAME
    CREATED_AT        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                             -- CREATED TIME
    UPDATED_AT        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- UPDATED TIME
    KEY               IDX_GITHUB_LOGIN (GITHUB_LOGIN)                                  -- SEARCH INDEX
);

-- =========================
-- ACCESS_TOKEN
-- =========================
CREATE TABLE ACCESS_TOKEN
(
    ID           BIGINT AUTO_INCREMENT PRIMARY KEY,                                 -- PK
    USER_ID      BIGINT       NOT NULL,                                             -- FK -> USER_ACCOUNT
    ACCESS_TOKEN VARCHAR(255) NOT NULL UNIQUE,                                      -- GITHUB ACCESS TOKEN
    TOKEN_TYPE   VARCHAR(20) DEFAULT 'BEARER',                                      -- TOKEN TYPE
    SCOPE        VARCHAR(500) NULL,                                                 -- TOKEN SCOPE
    EXPIRES_AT   TIMESTAMP NULL,                                                    -- EXPIRATION TIME
    CREATED_AT   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,                             -- CREATED TIME
    UPDATED_AT   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- UPDATED TIME

    CONSTRAINT FK_ACCESS_TOKEN_USER FOREIGN KEY (USER_ID) REFERENCES USER_ACCOUNT (USER_ID) ON DELETE CASCADE,

    KEY          IDX_TOKEN_USER (USER_ID)
);

-- =========================
-- GITHUB_REPOSITORY
-- =========================
CREATE TABLE GITHUB_REPOSITORY
(
    REPOSITORY_ID        BIGINT AUTO_INCREMENT PRIMARY KEY,                              -- PRIMARY KEY
    OWNER_USER_ID        BIGINT       NOT NULL,                                          -- REPO OWNER
    GITHUB_REPOSITORY_ID BIGINT       NOT NULL UNIQUE,                                   -- GITHUB REPO ID
    REPOSITORY_NAME      VARCHAR(100) NOT NULL,                                          -- REPO NAME
    REPOSITORY_FULL_NAME VARCHAR(200) NOT NULL UNIQUE,                                   -- OWNER/NAME
    DEFAULT_BRANCH       VARCHAR(100) NULL,                                              -- DEFAULT BRANCH
    DESCRIPTION          TEXT NULL,                                                      -- REPO DESCRIPTION
    GITHUB_REPOSITORY_URL VARCHAR(255) NULL,                                             -- GITHUB URL
    IS_PRIVATE           TINYINT(1) NOT NULL DEFAULT 0,                                  -- PRIVATE 여부
    LAST_SYNCED_AT       DATETIME NULL,                                                  -- LAST SYNC TIME
    CREATED_AT           DATETIME DEFAULT CURRENT_TIMESTAMP,                             -- CREATED TIME
    UPDATED_AT           DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- UPDATED TIME

    CONSTRAINT FK_GITHUB_REPOSITORY_OWNER FOREIGN KEY (OWNER_USER_ID) REFERENCES USER_ACCOUNT (USER_ID) ON DELETE CASCADE,

    KEY            IDX_REPOSITORY_OWNER (OWNER_USER_ID),
    KEY            IDX_REPOSITORY_SYNC_TIME (LAST_SYNCED_AT)
);

-- =========================
-- COMMIT_LOG
-- =========================
CREATE TABLE COMMIT_LOG
(
    ID                BIGINT AUTO_INCREMENT PRIMARY KEY,  -- PRIMARY KEY
    REPOSITORY_ID     BIGINT   NOT NULL,                  -- FK -> GITHUB_REPOSITORY
    AUTHOR_ID         BIGINT NULL,                        -- FK -> USER_ACCOUNT (DELETED USER ALLOWED)
    GITHUB_COMMIT_SHA CHAR(40) NOT NULL,                  -- COMMIT SHA
    COMMITTED_AT      DATETIME NOT NULL,                  -- COMMIT DATE
    MESSAGE           TEXT     NOT NULL,                  -- COMMIT MESSAGE
    HTML_URL          VARCHAR(255) NULL,                  -- COMMIT URL
    ADDITIONS         INT      DEFAULT 0,                 -- LINES ADDED
    DELETIONS         INT      DEFAULT 0,                 -- LINES DELETED
    TOTAL_CHANGES     INT      DEFAULT 0,                 -- TOTAL LINES CHANGED
    FILES_CHANGED     INT NULL,                           -- NUMBER OF CHANGED FILES
    CREATED_AT        DATETIME DEFAULT CURRENT_TIMESTAMP, -- CREATED TIME

    CONSTRAINT FK_COMMIT_REPO FOREIGN KEY (REPOSITORY_ID) REFERENCES GITHUB_REPOSITORY (REPOSITORY_ID) ON DELETE CASCADE,
    CONSTRAINT FK_COMMIT_AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES USER_ACCOUNT (USER_ID) ON DELETE SET NULL,
    UNIQUE KEY UK_COMMIT_REPO_SHA (REPOSITORY_ID,GITHUB_COMMIT_SHA),

    KEY               IDX_COMMIT_AUTHOR_TIME (AUTHOR_ID,COMMITTED_AT),
    KEY               IDX_COMMIT_REPO_TIME (REPOSITORY_ID,COMMITTED_AT)
);

-- =========================
-- COMMIT_FILE_CHANGE
-- =========================
CREATE TABLE COMMIT_FILE_CHANGE
(
    ID                BIGINT AUTO_INCREMENT PRIMARY KEY,                     -- PRIMARY KEY
    COMMIT_ID         BIGINT       NOT NULL,                                 -- FK -> COMMIT_LOG
    FILENAME          VARCHAR(500) NOT NULL,                                 -- FILE NAME
    PREVIOUS_FILENAME VARCHAR(500) NULL,                                     -- RENAMED PREVIOUS NAME
    STATUS            ENUM('ADDED','MODIFIED','REMOVED','RENAMED') NOT NULL, -- FILE CHANGE STATUS
    ADDITIONS         INT          NOT NULL DEFAULT 0,                       -- ADDED LINES
    DELETIONS         INT          NOT NULL DEFAULT 0,                       -- DELETED LINES
    CHANGES           INT          NOT NULL DEFAULT 0,                       -- TOTAL LINE CHANGES
    PATCH             MEDIUMTEXT NULL,                                       -- DIFF PATCH DATA
    CREATED_AT        DATETIME              DEFAULT CURRENT_TIMESTAMP,       -- CREATED TIME

    CONSTRAINT FK_COMMIT_FILE_COMMIT FOREIGN KEY (COMMIT_ID) REFERENCES COMMIT_LOG (ID) ON DELETE CASCADE,

    KEY               IDX_FILE_COMMIT (COMMIT_ID),
    KEY               IDX_FILE_FILENAME (FILENAME(255))
);

-- =========================
-- COMMIT_ANALYSIS
-- =========================
CREATE TABLE COMMIT_ANALYSIS
(
    ID              BIGINT AUTO_INCREMENT PRIMARY KEY,          -- PRIMARY KEY
    COMMIT_ID       BIGINT NOT NULL UNIQUE,                     -- 1:1 WITH COMMIT_LOG
    FLAGGED_COUNT   INT    NOT NULL DEFAULT 0,                  -- TOTAL FLAGGED TOKENS COUNT
    SWEAR_COUNT     INT    NOT NULL DEFAULT 0,                  -- SWEAR WORD COUNT
    EXCLAIM_COUNT   INT    NOT NULL DEFAULT 0,                  -- EXCLAMATION COUNT
    EMOJI_COUNT     INT    NOT NULL DEFAULT 0,                  -- EMOJI COUNT
    SENTIMENT       ENUM('POSITIVE','NEUTRAL','NEGATIVE') NULL, -- ANALYSIS RESULT
    SENTIMENT_SCORE DECIMAL(5, 2) NULL,                         -- SENTIMENT SCORE (-1.00 ~ 1.00)
    ANALYZED_AT     DATETIME        DEFAULT CURRENT_TIMESTAMP,  -- ANALYSIS TIME

    CONSTRAINT FK_COMMIT_ANALYSIS_COMMIT FOREIGN KEY (COMMIT_ID) REFERENCES COMMIT_LOG (ID) ON DELETE CASCADE
);

-- =========================
-- FLAGGED_TOKEN
-- =========================
CREATE TABLE FLAGGED_TOKEN
(
    ID         BIGINT AUTO_INCREMENT PRIMARY KEY,                                         -- PRIMARY KEY
    COMMIT_ID  BIGINT       NOT NULL,                                                     -- FK -> COMMIT_LOG
    TOKEN      VARCHAR(100) NOT NULL,                                                     -- FLAGGED WORD OR EMOJI
    TOKEN_TYPE ENUM('SWEAR','SLANG','EMOJI','EMPHASIS','OTHER') NOT NULL DEFAULT 'OTHER',
    WEIGHT     INT          NOT NULL DEFAULT 1,                                           -- IMPORTANCE WEIGHT
    CREATED_AT DATETIME              DEFAULT CURRENT_TIMESTAMP,                           -- CREATED TIME

    CONSTRAINT FK_FLAGGED_TOKEN_COMMIT FOREIGN KEY (COMMIT_ID) REFERENCES COMMIT_LOG (ID) ON DELETE CASCADE,
    UNIQUE KEY UK_COMMIT_TOKEN (COMMIT_ID,TOKEN,TOKEN_TYPE),

    KEY        IDX_FLAGGED_COMMIT (COMMIT_ID),
    KEY        IDX_FLAGGED_TOKEN_TYPE (TOKEN_TYPE)
);