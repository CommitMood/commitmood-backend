import os
import random
import uuid

from faker import Faker

fake = Faker()


# --- Random realistic source filename generator ---
def random_source_file():
    groups = [
        ("python", ["py"], ["app", "service", "model", "utils", "worker"]),
        ("java", ["java"], ["src/main/java/com/example/app", "src/test/java/com/example/app"]),
        ("javascript", ["js", "jsx"], ["src", "src/components", "src/hooks"]),
        ("typescript", ["ts", "tsx"], ["src", "src/pages", "src/components"]),
        ("c", ["c", "h"], ["src", "include"]),
        ("cpp", ["cpp", "hpp", "cc"], ["src", "include"]),
        ("frontend", ["vue", "svelte", "css", "scss"], ["src", "src/views", "src/styles"]),
        ("config", ["yml", "yaml", "json", "xml", "properties"], ["config", "resources"]),
        ("misc", ["md", "sql", "sh", "dockerfile"], ["docs", "scripts", "."]),
    ]
    _, exts, dirs = random.choice(groups)
    ext = random.choice(exts)
    directory = random.choice(dirs)
    filename = fake.file_name(extension=ext)
    if directory == ".":
        return filename
    return f"{directory}/{filename}"


# --- Commit message generator ---
commit_types = ["feat", "fix", "refactor", "chore", "docs", "test", "perf", "build", "ci"]
commit_subjects = [
    # Feature additions (30+)
    "add login API", "implement oauth2 refresh token", "add commit detail page", "add github webhook listener",
    "add user notification center", "add refreshable dashboard widgets", "add repo search filtering",
    "add advanced commit history timeline", "add comment reaction support", "add cache invalidation logic",
    "add sentiment color tags", "add markdown rendering support", "add S3 artifact upload handler",
    "add user preference theme sync", "add dark/light auto toggle", "add commit trend analytics",
    "add repository cleanup utility", "add mobile responsive UI layout", "add archived repo viewer",
    "add system health check endpoint", "add PDF export for report", "add detailed diff viewer",
    "add concurrency-safe batch processor", "add rate-limit safeguard", "add two-factor authentication",
    "add auto-tag commit classification", "add contribution heatmap", "expand repo metadata fields",
    "support large repo synchronization", "enable incremental diff batching", "introduce commit warnings page",

    # Bug fixes (20+)
    "fix ranking sort order", "fix null pointer in sync pipeline", "fix data loss in merge operation",
    "fix pagination jump issue", "fix incorrect timezone parsing", "fix delayed sync schedule",
    "fix flaky test cases in CI", "fix emoji parsing failure", "fix incorrect diff output in UI",
    "fix deadlock in batch processing", "fix non-deterministic commit ordering", "fix analysis summary mismatch",
    "fix UI freeze on large dataset", "fix high memory usage in tokenizer", "fix incorrect branch reference",
    "fix event loop concurrency inconsistency", "fix repo watcher crash", "fix wrong color-coding in diff view",

    # Refactoring (15+)
    "refactor service layer", "refactor token extraction logic", "refactor API routing structure",
    "refactor commit sentiment pipeline", "refactor DTO structure naming", "refactor caching strategy for repos",
    "refactor parsing engine for performance", "refactor concurrency locks", "refactor job scheduler initialization",
    "refactor notification dispatch queue", "refactor event-driven architecture", "refactor exception flow handling",
    "refactor repo-index sweep job", "refactor outdated batch scheduler", "refactor database migration logic",

    # Performance & scalability
    "optimize pagination logic", "reduce cold-start latency", "optimize commit batch sync throughput",
    "optimize query performance on dashboard", "optimize serializer for large payload",
    "reduce lock contention in writer threads", "reduce I/O load during diff generation",
    "parallelize large repo analysis", "improve cache hit rate", "compress stored patch payloads",
    "index heavy-read tables", "shard analytics by user", "improve batch stability",

    # Chore / cleaning
    "remove unused legacy code", "cleanup deprecated feature flags", "cleanup unused imports",
    "remove obsolete CI config", "remove orphaned schema fields", "remove redundant migration scripts",
    "cleanup logging verbosity", "cleanup old repository snapshots", "drop unused test fixtures",
    "cleanup local debug configuration", "remove dead code in transformers", "remove duplicate mapping layer",

    # Documentation & developer experience
    "update README with setup instructions", "improve API docs coverage", "document internal service design",
    "add architecture overview diagram", "add commit quality guideline", "update contribution guide",
    "add onboarding quickstart", "restructure docs navigation", "add examples to SDK samples",
    "update environment variable table", "improve test result report formatting",

    # Testing / QA
    "increase test coverage", "add load tests for batch pipeline", "mock GitHub API for integration tests",
    "add fuzz testing for commit tokenizer", "add stress test mode", "add E2E test suite for OAuth flow",
    "add static analysis rule set", "add regression tests for diff engine", "improve flaky test stabilization",
    "validate performance under spike load", "add canary deployment test",

    # 낮은 퀄리티 혹은 불량 커밋 (20+개 포함)
    "temporary bypass patch", "ugly workaround injection", "dirty quickfix for deploy",
    "lazy fix please ignore", "this is nonsense change", "wtf is this even for",
    "hacky unstable sync", "shit patch emergency hotfix", "damn dirty rollback",
    "bloated workaround commit", "messy hotfix before demo", "fix later TODO spam",
    "why did we ship this", "unstable quickfix again", "remove something idk",
    "nonsense patch no tests", "emergency deploy damn", "refactor??? no idea why",
    "please don't ask why", "commit before sleep wtf"
]


def make_commit_message():
    base = f"{random.choice(commit_types)}: {random.choice(commit_subjects)}"

    # 20~30% 확률 부적절 표현 추가
    if random.random() < random.uniform(0.20, 0.30):
        bad_words = ["shit", "wtf", "bull", "damn", "bloated", "messy", "lazy", "hack",
                     "workaround", "quickfix", "temporary", "dirty", "ugly", "nonsense", "unstable", "why"]
        injected = base + " " + random.choice(bad_words)
        return injected.replace("'", "''")
    return base.replace("'", "''")


# ----------------------------
# Dummy Data Volume
# ----------------------------
USER_COUNT = 50
REPO_COUNT = 120
COMMIT_COUNT = 20_000
FILE_CHANGE_COUNT = 30_000


def sha(): return uuid.uuid4().hex.ljust(40, '0')


os.makedirs("generated", exist_ok=True)
f_user = open("generated/1_user.sql", "w")
f_repo = open("generated/2_repo.sql", "w")
f_commit = open("generated/3_commit.sql", "w")
f_file = open("generated/4_commit_file.sql", "w")
f_analysis = open("generated/5_analysis.sql", "w")
f_token = open("generated/6_token.sql", "w")

##############################################
# USER_ACCOUNT
##############################################
user_ids = []
for i in range(USER_COUNT):
    uid = i + 1
    user_ids.append(uid)
    f_user.write(
        f"INSERT INTO USER_ACCOUNT (ID,GITHUB_USER_ID,GITHUB_LOGIN,GITHUB_EMAIL,GITHUB_NAME)"
        f" VALUES({uid},{fake.random_int() % 9999999},'{fake.user_name()}','{fake.email()}','{fake.name()}');\n"
    )

##############################################
# GITHUB_REPO
##############################################
repo_ids = []
for i in range(REPO_COUNT):
    rid = i + 1
    repo_ids.append(rid)
    owner = random.choice(user_ids)
    name = fake.word() + "-" + fake.word()
    full = f"user{owner}/{name}"
    f_repo.write(
        f"INSERT INTO GITHUB_REPO (ID,USER_ACCOUNT_ID,GITHUB_REPO_ID,GITHUB_REPO_NAME,GITHUB_REPO_FULL_NAME)"
        f" VALUES({rid},{owner},{100000 + rid},'{name}','{full}');\n"
    )

##############################################
# COMMIT_LOG
##############################################
commit_ids = []
commit_messages = {}

for i in range(COMMIT_COUNT):
    cid = i + 1
    commit_ids.append(cid)

    msg = make_commit_message()
    commit_messages[cid] = msg

    f_commit.write(
        f"INSERT INTO COMMIT_LOG (ID,GITHUB_REPO_ID,USER_ACCOUNT_ID,GITHUB_COMMIT_SHA,COMMITTED_AT,MESSAGE,ADDITIONS,DELETIONS,TOTAL_CHANGES)"
        f" VALUES({cid},{random.choice(repo_ids)},{random.choice(user_ids)},'{sha()}',NOW() - INTERVAL {random.randint(0, 365)} DAY,"
        f"'{msg}',{random.randint(0, 300)},{random.randint(0, 100)},{random.randint(0, 400)});\n"
    )

##############################################
# COMMIT_FILE_CHANGE
##############################################
for i in range(FILE_CHANGE_COUNT):
    fid = i + 1
    c = random.choice(commit_ids)
    name = random_source_file()
    status = random.choice(['ADDED', 'MODIFIED', 'REMOVED', 'RENAMED'])
    add, delete = random.randint(0, 200), random.randint(0, 150)

    f_file.write(
        f"INSERT INTO COMMIT_FILE_CHANGE (ID,COMMIT_LOG_ID,FILENAME,STATUS,ADDITIONS,DELETIONS,CHANGES)"
        f" VALUES({fid},{c},'{name}','{status}',{add},{delete},{add + delete});\n"
    )

##############################################
# COMMIT_ANALYSIS & TOKEN Extraction
##############################################
# flagged_token 기준으로 사용할 "비정상 commit 패턴 단어"
keywords = [
    # 욕설/격앙 표현
    "wtf", "shit", "damn", "fuck", "bull", "nonsense",

    # 급한 땜빵식 커밋 판단 요소
    "quickfix", "temporary", "temp", "hotfix", "workaround", "bypass",
    "hack", "hacky", "unstable", "dirty", "messy", "bloated",

    # 책임 회피·설명 부족·불확실한 표현
    "idk", "ignore", "later", "maybe", "why", "unknown",

    # 개발 태도 문제 가능성
    "lazy", "please ignore", "no test", "spam", "unfinished", "sleep",
]

for cid in commit_ids:

    score = round(random.uniform(-1, 1), 2)
    senti = "POSITIVE" if score > 0.3 else "NEGATIVE" if score < -0.3 else "NEUTRAL"

    f_analysis.write(
        f"INSERT INTO COMMIT_ANALYSIS (COMMIT_LOG_ID,SENTIMENT,SENTIMENT_SCORE,FLAGGED_COUNT,SWEAR_COUNT,EXCLAIM_COUNT,EMOJI_COUNT)"
        f" VALUES({cid},'{senti}',{score},{random.randint(0, 5)},{random.randint(0, 2)},{random.randint(0, 3)},{random.randint(0, 3)});\n"
    )

    msg_lower = commit_messages[cid].lower()
    found = [k for k in keywords if k in msg_lower][:3]  # max 3 tokens

    for token in found:
        f_token.write(
            f"INSERT INTO FLAGGED_TOKEN (COMMIT_LOG_ID,TOKEN,TOKEN_TYPE,WEIGHT)"
            f" VALUES({cid},'{token}','NEGATIVE',{random.randint(1, 3)});\n"
        )

print("\n🎉 SQL 파일 생성 완료 → generated/ 폴더 확인\n")
