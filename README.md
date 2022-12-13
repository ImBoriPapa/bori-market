# Bori Market API Server
### 현재 버전 정보 1.0.1
- 2022/11/27
- 각 모듈 기본 기능 구현
#### 진행중인 다음 버전 변경 사항
##### 12.14 피드백 내용입니다.
- 전체적인 코드 개선이 필요함 : 코드 퀄리티가 이것을 받쳐주지 못함 → 전체적인 리펙토링
- git으로 버전 관리를 했다고하는데 막상 repository를 보면 main branch만 있네요. 단일 branch로 작업하신건지, 아니면 다른 branch들은 로컬이나 다른 repo에 있는건지 의문이 듭니다.→현재 단일 branch → 작업별 branch로 변경
- 거의 비슷한 스택으로 하신거 같네요. 추후 각 서비스 별로 어떤 기술이 더 적합할 거 같다던지 그런 플랜이 있나요?→ 추 후 도입하거나 변경예정 기술 어필
- 만약 한 컴포넌트에 장애가 발생됐을 때 다른 컴포넌트는 어떻게 되나요?→ 에러 핸들링 설명 및 보완
- 서비스 운영 관점의 설명이 부족한 것 같습니다. → 상황별 스토리 만들기
- 여러 고민을 녹여내는 것이 더 인상깊은 포트폴리오 → 좀 더 디테일한 고민해보기

### 토큰 인증기반 로그인기능 판매 글 게시판 API 서버입니다.


## 프로젝트 전체 구조
![Untitled](https://user-images.githubusercontent.com/98242564/207268478-c4abc9c6-fb4b-40bd-b0f9-86d4a31fa55b.png)

## 구현 기능
### 계정
- 계정생성
- 계정조회
- 계정 비밀번호 변경
- 계정 이메일 변경
### 로그인
- 프로필 조회
- 프로필 닉네임 변경
- 프로필 주소 변경
- 프로필 주소 검색 범위 수정
- 프로필 이미지 변경
### 주소
- 주소정보 검색
### 판매 글
- 판매 글 생성
- 판매 글 리스트 조회
- 판매 글 내용 조회
- 판매 글 수정
- 판매 글 삭제


## API 문서
[API 문서 링크로 보기](https://imboripapa.github.io/api-docs/)

## [security-service 모듈 바로가기](https://github.com/ImBoriPapa/bori-market/tree/main/security-service)
## [frm-service 모듈 바로가기](https://github.com/ImBoriPapa/bori-market/tree/main/frm-service)
## [trade-service 모듈 바로가기](https://github.com/ImBoriPapa/bori-market/tree/main/trade-service)
## [address-service 모듈 바로가기](https://github.com/ImBoriPapa/bori-market/tree/main/address-service)