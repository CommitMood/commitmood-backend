![rect](https://capsule-render.vercel.app/api?type=rect&color=gradient&text=🌙%20CommitMood%20&fontAlign=25&fontSize=25&textBg=true&desc=개발자의%20감정을%20커밋%20메시지로%20%20읽어내는%20서비스&descAlign=69&descAlignY=50)

## 📋 목차

- [📖 프로젝트 소개](#-프로젝트-소개)
- [🎯 프로젝트 목표](#-프로젝트-목표)
- [🛠️ 기술 스택](#-기술-스택)
- [🧩 기능 요구사항](#-기능-요구사항)
- [🌱 Git Branching Strategy](#-git-branching-strategy)
- [📝 Commit Message Convention](#-commit-message-convention)
- [📚 상세 문서](#-상세-문서)

<br>

## 📖 프로젝트 소개

> [!NOTE]
> 개발자의 GitHub 커밋 메시지에서 감정, 작업 패턴, 활동 리듬을 분석하여 <br>
> 개발 경험을 더욱 의미 있게 만들어 주는 웹 서비스입니다. <br>
> 개발 여정을 돌아보고, 평소에는 보이지 않았던 개발 습관, 감정의 변동, 생산성 신호를 확인할 수 있습니다.

### 핵심 기능

| 구분                   | 설명                                                    |
|----------------------|-------------------------------------------------------|
| 🚗 **커밋 데이터 분석 자동화** | GitHub REST API를 활용하여 사용자의 커밋 메시지를 자동으로 수집/분석합니다.     |
| ✨ **감정 분석**          | 커밋 메시지에서 긍정/부정/감탄사/감정적 표현을 탐지합니다.                     |
| 📊 **개발 패턴 시각화**     | "가장 많이 커밋한 개발자"를 다양한 기준으로 실시간 랭킹을 제공합니다.              |
| 🏆 **기여도 지표 제공**     | 기간별 핵심 단어, 감정 편향 등을 분석/시각화하여 사용자 간의 경쟁과 재미 요소를 강화합니다. |
| 🔍 **개인 히스토리 관리**    | 사용자는 자신의 커밋 감정 기록과 단어 사용 통계를 확인할 수 있습니다.              |

<br>

## 🎯 프로젝트 목표

> [!IMPORTANT]
> GitHub 커밋 메시지를 수집•분석하여 감정, 패턴, 습관을 시각화하는 서비스 개발

### 💰 주요 가치
- **셀프 피드백 도구**
    - 개발자가 스스로의 개발 습관·감정 상태를 객관적으로 파악
- **데이터 기반 자기 성찰**
    - commit count가 아닌 ‘내용과 감정’을 분석
- **생산성 시각화**
    - 시간대/요일/주기별 개발 리듬 파악
- **문서 자동화**
    - 자동 회고/요약 레포트 제공

### 💁🏻‍♂️ 주요 사용자
- 개발 패턴을 분석해 보고 싶은 사람
- GitHub를 자주 사용하는 개발자
- 감정 분석/헬스 체크가 필요한 팀
- 주기적 회고가 필요한 개발자

<br>

## 🛠️ 기술 스택

### 🔙 Backend
- Java 21
- Spring Boot 3.x
- MyBatis
- MySQL
- GitHub REST API

### 🏢 Infra / Auth
- Docker
- GitHub OAuth

<br>

## 🧩 기능 요구사항

### 📌 주요 기능
- GitHub OAuth 로그인 기반 사용자 인증
- GitHub 커밋 메시지 자동 수집 및 저장
- 커밋 메시지 기반 감정 키워드 분석
- 개발 패턴 및 감정 분포 시각화
- 사용자 및 기간별 랭킹 제공

> [!TIP]
> 상세 기능 요구사항은 요구사항 정의서를 참고해주세요.

<br>

## 🌱 Git Branching Strategy

### ⏳ GitHub Flow
```
master: 운용 브랜치
feature/#issue: 기능 개발 브랜치
```

### 💪🏻 Workflow
1) 이슈 생성
2) `feature` 브랜치에서 기능 개발
3) PR 생성
4) 코드 리뷰
5) `master`에 merge 및 배포

<br>

## 📝 Commit Message Convention

### 기본 형식
```plaintext
[gitmoji] type(#issue): subject
```

### Type
```
master
 └── develop
 ├── feature/{#issue}
 ├── fix/{#bug}      // 예비
 ├── hotfix/{#num}   // 예비
 └── docs/{#num}    // 예비
```

### Commit Prefixes
```plaintext
✨ feat: 새로운 기능 추가
🔨 fix: 버그 수정
📝 docs: 문서 수정
🎨 style: 코드 스타일 변경
♻️ refactor: 코드 리팩토링
⚡ perf: 성능 개선
🧪 test: 테스트 코드 추가/수정
🗂️ chore: 빌드/환경 설정 변경
🤖 ci: CI/CD 구성 변경
🚧 wip: 작업 중인 커밋
🚚 rename: 파일 또는 폴더 이름 변경
🧹 clean: 불필요한 파일/코드 제거
🔁 revert: 이전 커밋 되돌리기
```

### Example
```plaintext
✨ feat(domain): add commit mood analyzer
```

<br>

## 📚 상세 문서

- 요구사항 정의서
- Use-Case 다이어그램
- 클래스 다이어그램
- ER 다이어그램
- WBS & 간트차트
- 화면 설계서

<br>

[status-url]: #
[status-shield]: https://img.shields.io/badge/status-in%20development-yellow?style=for-the-badge
