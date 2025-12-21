# CommitMood API Documentation

GitHub 커밋 기반 감정 분석 서비스 API 문서

- **Version**
  - v1 (OAS 3.1)
- **API Docs**
  - /v3/api-docs
- **Base URL**
  - /api

---

## 1. User Command API
사용자 정보 생성 및 수정 Command API

### PATCH /users/{id}/github/profile
GitHub 프로필 정보 업데이트

내부 사용자 ID 기준 GithubProfileUpdateRequest 데이터를 전달 받아 사용자 GitHub 정보를 갱신합니다.

**Path Parameters**
- id (int64): 수정 대상 내부 사용자 ID

**Request Body**
```json
{
  "githubEmail": "shkim@example.com",
  "githubAvatarUrl": "https://avatars.githubusercontent.com/u/1234567?v=4",
  "githubName": "devys"
}
```

**Responses**
- 200 수정 완료
- 400 잘못된 요청 데이터
- 404 사용자를 찾을 수 없음
- 500 서버 내부 오류

---

### DELETE /users/{id}
사용자 삭제

내부 userId에 해당하는 사용자를 삭제합니다.

**Path Parameters**
- id (int64): 삭제 대상 사용자 ID

**Responses**
- 204 삭제 완료
- 404 사용자를 찾을 수 없음
- 500 서버 내부 오류

---

## 2. Ranking API
사용자 대상, 전체 랭킹 API

### GET /ranking
전체 사용자 랭킹 조회

**Query Parameters**
- option (string)
    - commit_count: 총 커밋 수 기준
    - flagged_count: 플래그 토큰 포함 커밋 수 기준
    - swear_count: 욕설 토큰 포함 커밋 수 기준
    - sentiment_score: 평균 감정 점수 기준
    - recent: 최근 활동 기준
- page (int32, default=1)
- per_page (int32, default=30)

**Response**
```json
{
  "rankings": [
    {
      "userAccountId": 1,
      "githubLogin": "devys",
      "githubAvatarUrl": "https://avatars.githubusercontent.com/u/1234567?v=4",
      "totalCommits": 152,
      "totalFlagged": 12,
      "totalSwear": 3,
      "avgSentimentScore": 0.78,
      "lastCommitAt": "2025-12-18T05:04:32.561Z"
    }
  ],
  "page": 1,
  "perPage": 30,
  "totalCount": 300
}
```

---

### GET /ranking/{userAccountId}
특정 사용자 랭킹 상세 조회

**Path Parameters**
- userAccountId (int64)

**Query Parameters**
- detail=repo
    - 사용자의 저장소별 커밋 통계 목록
- detail=flagged
    - 플래그 토큰 유형별 커밋 통계
- detail=sentiment
    - 감정 분석 결과 분포 통계
- page (int32, default=1)
- per_page (int32, default=30)

**Response**
```json
{
  "detailType": "repo",
  "detailData": [
    {
      "repoId": 10,
      "repoName": "commitmood-backend",
      "commitCount": 87
    }
  ],
  "page": 1,
  "perPage": 30
}
```

---

## 3. Commit Analysis API

### GET /commits/{commitLogId}/tokens
플래그 토큰 목록 조회

**Path Parameters**
- commitLogId (int64)
- tokenType 값 설명
  - SWEAR: 욕설 토큰
  - EXCLAIM: 과도한 느낌표
  - EMOJI: 감정 표현 이모지
  - NEGATIVE_WORD: 부정 감정 단어
- sentiment 값
  - POSITIVE
  - NEUTRAL
  - NEGATIVE

**Response**
```json
[
  {
    "token": "damn",
    "tokenType": "SWEAR",
    "weight": 5
  },
  {
    "token": "!!!",
    "tokenType": "EXCLAIM",
    "weight": 2
  }
]
```

**Responses**
- 200 조회 성공
- 404 커밋을 찾을 수 없음

---

### GET /commits/{commitLogId}/analysis
커밋 분석 정보 조회

**Path Parameters**
- commitLogId (int64)

**Response**
```json
{
  "commitLogId": 1,
  "flaggedCount": 3,
  "swearCount": 1,
  "exclaimCount": 2,
  "emojiCount": 4,
  "sentiment": "POSITIVE",
  "sentimentScore": 0.85,
  "analyzedAt": "2025-12-01T12:00:00"
}
```

**Responses**
- 200 조회 성공
- 404 커밋 분석 정보를 찾을 수 없음

---

## 4. User Search API

### GET /users/search
사용자 검색

Response는 검색 조건에 매칭되는 사용자 목록입니다.

**Query Parameters**
- keyword (string)

**Response**
```json
[
  {
    "id": 1,
    "githubLogin": "devys",
    "githubAvatarUrl": "https://avatars.githubusercontent.com/u/1234567?v=4"
  }
]
```

---

### GET /users/search/page
사용자 검색 (페이징)

**Query Parameters**
- keyword (string)
  - 조회 우선순위
    1. email 파라미터가 존재할 경우 email prefix 검색
    2. githubLogin 파라미터가 존재할 경우 githubLogin prefix 검색
    3. 그 외의 경우 name prefix 검색
- page (int32, default=1)
- size (int32, default=10)

**Response**
```json
{
  "content": [
    {
      "id": 1,
      "githubLogin": "devys",
      "githubAvatarUrl": "https://avatars.githubusercontent.com/u/1234567?v=4"
    }
  ],
  "page": 1,
  "size": 10,
  "totalCount": 45,
  "hasNext": true,
  "totalPages": 5
}
```

---

## 5. User Query API

### GET /users
사용자 조회 (쿼리 기반)

**Query Parameters**
- email (string)
- githubLogin (string)
- name (string)

**Response**
```json
{
  "id": 12,
  "githubUserId": 9876543,
  "githubLogin": "devys",
  "githubEmail": "devys@example.com",
  "githubAvatarUrl": "https://avatars.githubusercontent.com/u/1234567?v=4",
  "githubName": "Devys Kim",
  "lastSyncedAt": "2025-12-11T14:23:00",
  "createdAt": "2025-10-01T11:20:00"
}
```

---

## 6. GitHub Repo API

### GET /users/{userId}/repos
특정 사용자 GitHub Repo 리스트 조회

**Response**
```json
{
  "repoList": [
    {
      "id": 1,
      "repoName": "commitmood-backend",
      "repoUrl": "https://github.com/CommitMood/commitmood-backend"
    }
  ],
  "count": 1
}
```

---

### GET /repos/{repoId}
GitHub Repo 단건 조회

**Response**
```json
{
  "id": 12,
  "userAccountId": 5,
  "githubRepoId": 987654321,
  "name": "commitmood-backend",
  "fullName": "CommitMood/commitmood-backend",
  "defaultBranch": "main",
  "description": "CommitMood backend API service",
  "url": "https://github.com/CommitMood/commitmood-backend",
  "isPrivate": false,
  "lastSyncedAt": "2025-12-10T14:20:30"
}
```

---

### GET /repos/{repoId}/commits
GitHub Repo Commit 조회

**Response**
```json
[
  {
    "id": 1,
    "repoId": 10,
    "userAccountId": 5,
    "sha": "2b3010ecff76...",
    "message": "fix: handle null pointer in ranking api",
    "committedAt": "2025-12-01T10:15:30",
    "additions": 10,
    "deletions": 2,
    "totalChanges": 12
  }
]
```

---

### GET /repos/search
GitHub Repo 검색 (비페이징)

**Query Parameters**
- keyword (string)

**Response**
```json
{
  "repoList": [
    {
      "id": 1,
      "repoName": "commitmood-backend",
      "repoUrl": "https://github.com/CommitMood/commitmood-backend"
    }
  ],
  "count": 1
}
```

---

### GET /repos/search/page
GitHub Repo 검색 (페이징)

**Query Parameters**
- keyword (string)
- page (int32, default=1)
- size (int32, default=10)

**Response**
```json
{
  "content": [
    {
      "id": 12,
      "userAccountId": 5,
      "githubRepoId": 987654321,
      "name": "commitmood-backend",
      "fullName": "CommitMood/commitmood-backend",
      "defaultBranch": "main",
      "description": "CommitMood backend API service",
      "url": "https://github.com/CommitMood/commitmood-backend",
      "isPrivate": false,
      "lastSyncedAt": "2025-12-10T14:20:30"
    }
  ],
  "page": 1,
  "size": 10,
  "totalCount": 20,
  "hasNext": true,
  "totalPages": 2
}
```

---

## 7. User Streak API

### GET /streak/{userAccountId}
사용자 커밋 스트릭 조회

**Query Parameters**
- option (string): week | month | year

**Response**
```json
{
  "currentStreak": 7,
  "longestStreak": 21,
  "totalCommitDays": 150,
  "dailyCommits": {
    "2025-12-01": 2,
    "2025-12-02": 1
  }
}
```
