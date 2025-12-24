import os
import random
import uuid
from faker import Faker

fake = Faker()


# =========================
# util
# =========================
def sha():
    return uuid.uuid4().hex.ljust(40, "0")


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

    return filename if directory == "." else f"{directory}/{filename}"


# =========================
# commit message data (절대 축소 금지)
# =========================
commit_types = ["feat", "fix", "refactor", "chore", "docs", "test", "perf", "build", "ci"]

commit_subjects = [
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
    "fix ranking sort order", "fix null pointer in sync pipeline", "fix data loss in merge operation",
    "fix pagination jump issue", "fix incorrect timezone parsing", "fix delayed sync schedule",
    "fix flaky test cases in CI", "fix emoji parsing failure", "fix incorrect diff output in UI",
    "fix deadlock in batch processing", "fix non-deterministic commit ordering", "fix analysis summary mismatch",
    "fix UI freeze on large dataset", "fix high memory usage in tokenizer", "fix incorrect branch reference",
    "fix event loop concurrency inconsistency", "fix repo watcher crash", "fix wrong color-coding in diff view",
    "refactor service layer", "refactor token extraction logic", "refactor API routing structure",
    "refactor commit sentiment pipeline", "refactor DTO structure naming", "refactor caching strategy for repos",
    "refactor parsing engine for performance", "refactor concurrency locks", "refactor job scheduler initialization",
    "refactor notification dispatch queue", "refactor event-driven architecture", "refactor exception flow handling",
    "refactor repo-index sweep job", "refactor outdated batch scheduler", "refactor database migration logic",
    "optimize pagination logic", "reduce cold-start latency", "optimize commit batch sync throughput",
    "optimize query performance on dashboard", "optimize serializer for large payload",
    "reduce lock contention in writer threads", "reduce I/O load during diff generation",
    "parallelize large repo analysis", "improve cache hit rate", "compress stored patch payloads",
    "index heavy-read tables", "shard analytics by user", "improve batch stability",
    "remove unused legacy code", "cleanup deprecated feature flags", "cleanup unused imports",
    "remove obsolete CI config", "remove orphaned schema fields", "remove redundant migration scripts",
    "cleanup logging verbosity", "cleanup old repository snapshots", "drop unused test fixtures",
    "cleanup local debug configuration", "remove dead code in transformers", "remove duplicate mapping layer",
    "update README with setup instructions", "improve API docs coverage", "document internal service design",
    "add architecture overview diagram", "add commit quality guideline", "update contribution guide",
    "add onboarding quickstart", "restructure docs navigation", "add examples to SDK samples",
    "update environment variable table", "improve test result report formatting",
    "increase test coverage", "add load tests for batch pipeline", "mock GitHub API for integration tests",
    "add fuzz testing for commit tokenizer", "add stress test mode", "add E2E test suite for OAuth flow",
    "add static analysis rule set", "add regression tests for diff engine", "improve flaky test stabilization",
    "validate performance under spike load", "add canary deployment test",
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
    if random.random() < random.uniform(0.20, 0.30):
        bad_words = [
            "shit", "wtf", "bull", "damn", "bloated", "messy", "lazy", "hack",
            "workaround", "quickfix", "temporary", "dirty", "ugly",
            "nonsense", "unstable", "why"
        ]
        base += " " + random.choice(bad_words)
    return base.replace("'", "''")


# =========================
# volume
# =========================
USER_COUNT = 50
REPO_COUNT = 120
COMMIT_COUNT = 20_000
FILE_CHANGE_COUNT = 30_000


# =========================
# output
# =========================
os.makedirs("generated", exist_ok=True)

f_user = open("generated/01_user.sql", "w")
f_repo = open("generated/02_repo.sql", "w")
f_commit = open("generated/03_commit.sql", "w")
f_file = open("generated/04_commit_file.sql", "w")
f_analysis = open("generated/05_commit_analysis.sql", "w")
f_token = open("generated/06_flagged_token.sql", "w")


# =========================
# 1. user_account (ID 위임)
# =========================
for _ in range(USER_COUNT):
    f_user.write(
        f"INSERT INTO user_account "
        f"(github_user_id, github_login, github_email, github_name) "
        f"VALUES ("
        f"{fake.random_int() % 9_999_999},"
        f"'{fake.user_name()}',"
        f"'{fake.email()}',"
        f"'{fake.name()}'"
        f");\n"
    )


# =========================
# 2. github_repo
# =========================
for i in range(REPO_COUNT):
    owner_id = random.randint(1, USER_COUNT)
    name = fake.word() + "-" + fake.word()
    full = f"user{owner_id}/{name}"

    f_repo.write(
        f"INSERT INTO github_repo "
        f"(user_account_id, github_repo_id, github_repo_name, github_repo_full_name) "
        f"VALUES ("
        f"{owner_id},"
        f"{100_000 + i},"
        f"'{name}',"
        f"'{full}'"
        f");\n"
    )


# =========================
# 3. commit_log
# =========================
commit_messages = {}

for cid in range(1, COMMIT_COUNT + 1):
    msg = make_commit_message()
    commit_messages[cid] = msg
    days = random.randint(0, 365)

    f_commit.write(
        f"INSERT INTO commit_log "
        f"(github_repo_id, user_account_id, github_commit_sha, committed_at, "
        f"message, additions, deletions, total_changes) "
        f"VALUES ("
        f"{random.randint(1, REPO_COUNT)},"
        f"{random.randint(1, USER_COUNT)},"
        f"'{sha()}',"
        f"CURRENT_TIMESTAMP - INTERVAL '{days}' DAY,"
        f"'{msg}',"
        f"{random.randint(0,300)},"
        f"{random.randint(0,100)},"
        f"{random.randint(0,400)}"
        f");\n"
    )


# =========================
# 4. commit_file_change
# =========================
for _ in range(FILE_CHANGE_COUNT):
    add = random.randint(0, 200)
    delete = random.randint(0, 150)

    f_file.write(
        f"INSERT INTO commit_file_change "
        f"(commit_log_id, filename, status, additions, deletions, changes) "
        f"VALUES ("
        f"{random.randint(1, COMMIT_COUNT)},"
        f"'{random_source_file()}',"
        f"'{random.choice(['ADDED','MODIFIED','REMOVED','RENAMED'])}',"
        f"{add},"
        f"{delete},"
        f"{add + delete}"
        f");\n"
    )


# =========================
# 5. analysis + flagged_token
# =========================
keywords = [
    "wtf", "shit", "damn", "fuck", "bull", "nonsense",
    "quickfix", "temporary", "temp", "hotfix", "workaround", "bypass",
    "hack", "hacky", "unstable", "dirty", "messy", "bloated",
    "idk", "ignore", "later", "maybe", "why", "unknown",
    "lazy", "please ignore", "no test", "spam", "unfinished", "sleep",
]

for cid in range(1, COMMIT_COUNT + 1):
    score = round(random.uniform(-1, 1), 2)
    senti = "POSITIVE" if score > 0.3 else "NEGATIVE" if score < -0.3 else "NEUTRAL"

    f_analysis.write(
        f"INSERT INTO commit_analysis "
        f"(commit_log_id, sentiment, sentiment_score, "
        f"flagged_count, swear_count, exclaim_count, emoji_count) "
        f"VALUES ("
        f"{cid},"
        f"'{senti}',"
        f"{score},"
        f"{random.randint(0,5)},"
        f"{random.randint(0,2)},"
        f"{random.randint(0,3)},"
        f"{random.randint(0,3)}"
        f");\n"
    )

    msg_lower = commit_messages[cid].lower()
    found = [k for k in keywords if k in msg_lower][:3]

    for token in found:
        f_token.write(
            f"INSERT INTO flagged_token "
            f"(commit_log_id, token, token_type, weight) "
            f"VALUES ("
            f"{cid},"
            f"'{token}',"
            f"'SWEAR',"
            f"{random.randint(1,3)}"
            f");\n"
        )


print("\nSQL 파일 생성 완료 → generated/ 폴더 확인\n")