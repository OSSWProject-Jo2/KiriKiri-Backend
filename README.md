# 🎉 KiriKiri (끼리끼리)
> 함께할 사람을 찾는 파티 모집 서비스

## 프로젝트 소개
게임, 공부, 운동 등 다양한 분야에서 함께할 파티원을 모집할 수 있는 웹 서비스입니다.
비회원도 게시글을 열람할 수 있으며, Clerk 로그인 후 게시글 작성 및 수정/삭제, 파티 참여 신청이 가능합니다.

## 기술 스택
- **Backend** : Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA, Lombok
- **Database** : MySQL 8.0
- **Auth** : Clerk (JWT 기반 인증)
- **Frontend** : Next.js (별도 레포지토리)

## 프로젝트 구조

```
src/main/java/com/example/party_finder/
├── config/
│   ├── SecurityConfig.java          # CORS 및 JWT 인증 설정
│   └── GlobalExceptionHandler.java  # 전역 예외 처리 (404, 400 등)
├── controller/
│   ├── PostController.java          # 게시글 CRUD API
│   └── ApplicationController.java   # 참여 신청/조회/수락 API
├── domain/
│   ├── Post.java
│   ├── PostRepository.java
│   ├── Application.java
│   └── ApplicationRepository.java
├── dto/
│   ├── PostRequest.java
│   ├── PostResponse.java
│   └── ApplicationResponse.java     # 신청자 목록 응답 DTO
├── service/
│   ├── PostService.java
│   └── ApplicationService.java
├── DataInitializer.java
├── MainController.java
└── PartyFinderApplication.java
```

## 주요 기능
- 게시글 등록 / 수정 / 삭제 (Clerk 로그인 후 본인 글만 가능)
- 전체 목록 조회 및 단건 조회
- 카테고리별 필터링 (게임, 공부, 운동)
- 키워드 검색 (제목, 분야, 모임명)
- 파티 참여 신청 / 신청자 목록 조회 / 신청 수락
- 수락 시 트랜잭션으로 정원(currentMembers) 검증 및 증가
- Clerk JWT 토큰 기반 사용자 인증
- 비회원 게시글 열람 가능

## API 목록

### 게시글
| Method | URL | 설명 | 인증 필요 |
|--------|-----|------|----------|
| GET | /api/posts | 전체 목록 조회 | ❌ |
| GET | /api/posts/{id} | 단건 조회 | ❌ |
| GET | /api/posts/category/{category} | 카테고리 필터링 | ❌ |
| GET | /api/posts/search?keyword={keyword} | 키워드 검색 | ❌ |
| POST | /api/posts | 게시글 등록 | ✅ |
| PUT | /api/posts/{id} | 게시글 수정 | ✅ |
| DELETE | /api/posts/{id} | 게시글 삭제 | ✅ |

### 참여 신청
| Method | URL | 설명 | 인증 필요 |
|--------|-----|------|----------|
| POST | /api/posts/{postId}/applications | 참여 신청 | ✅ |
| GET | /api/posts/{postId}/applications | 신청자 목록 조회 (작성자만) | ✅ |
| PATCH | /api/posts/{postId}/applications/{id}/accept | 신청 수락 | ✅ |

## 로컬 실행 방법
1. MySQL에 `party_db` 데이터베이스 생성
2. `application.properties`에 DB 정보 입력
3. IntelliJ에서 `PartyFinderApplication` 실행
4. `http://localhost:8080/api/posts` 접속 확인

## 환경 설정 (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/party_db?serverTimezone=Asia/Seoul
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=YOUR_CLERK_JWKS_URI
```

## 개발 현황
- [x] Post 엔티티 설계
- [x] REST API 구현 (CRUD)
- [x] 카테고리 필터링
- [x] 키워드 검색
- [x] Clerk JWT 인증 연동
- [x] CORS 및 접근 권한 설정
- [x] Application 엔티티 설계
- [x] 참여 신청 / 조회 / 수락 API
- [x] 수락 시 정원 검증 및 currentMembers 증가 (트랜잭션)
- [x] 프론트엔드 API 응답 형태 일치 (ApplicationResponse DTO)
- [ ] 유저 프로필 API (/users/me)
