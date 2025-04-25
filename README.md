# Thread 스타일 SNS

## 🧵 프로젝트 설명
Thread SNS는 사용자가 자유롭게 게시글을 작성하고, 댓글로 이어지는 대화를 중심으로 소통할 수 있는 서비스입니다. 이 프로젝트는 Java & Spring Boot를 기반으로 구축되었으며, 좋아요, 팔로우, 사용자 프로필 등 SNS의 주요 기능들을 백엔드에서 구현했습니다.
게시글 간 연결 관계와 사용자 간 상호작용을 중심으로 설계되어 유연성을 고려한 RESTful API 중심 아키텍처로 개발되었습니다.

## 📁 프로젝트 구조
```
project-root
├── src
│   └── main
│       ├── java
│       │   └── com.project.board
│       │       ├── config         # 보안, 필터, 인코딩 설정
│       │       ├── controller     # API 컨트롤러
│       │       ├── exception      # 예외 처리
│       │       ├── model          # 엔티티, DTO, 에러 모델
│       │       ├── repository     # JPA 인터페이스
│       │       ├── service        # 비즈니스 로직
│       │       └── BoardApplication # 메인 실행 파일
│       └── resources
│           └── application.yaml   # 환경 설정
├── README.md
```

## 🧰 기술 스택
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Gradle
- Docker
- Postman
- Git, GitHub

## ✨ 주요 기능
- ✅ 회원가입 / 로그인 (JWT 기반 인증)
- ✅ 게시글 작성 / 조회 / 삭제
- ✅ 댓글을 통한 스레드 구조 형성
- ✅ 게시글 좋아요 / 좋아요 취소
- ✅ 로그인 사용자 기준, 좋아요 누른 유저와 팔로우 여부 반환
- ✅ 사용자 팔로우 / 언팔로우
- ✅ 사용자 프로필 조회


## 🔥 주요 도전 과제 및 해결 방법
### 1. 좋아요 + 팔로우 여부 동시 반환
- 좋아요 누른 유저 리스트 반환 시, 현재 로그인 사용자의 팔로우 여부 포함
- 스트림 `flatMap`과 DTO 조합으로 가독성 높게 처리

### 2. Docker 기반 환경 통합
- 백엔드 및 DB(PostgreSQL)를 Docker로 컨테이너화
- 프론트는 Docker 이미지 pull

### 3. API 문서 자동화 (Postman)
- Postman으로 테스트한 API를 문서화
- 공개 문서로 변환 → 다른 사용자도 쉽게 사용 가능하도록 공유

## 📌 API 문서

API 명세는 Postman으로 작성하고 문서화하였습니다.  
👇 아래 링크에서 확인하실 수 있습니다:

🔗 [API 문서 보러가기](https://documenter.getpostman.com/view/29995397/2sB2izDYjA)
