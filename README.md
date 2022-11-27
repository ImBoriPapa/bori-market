# bori-market

### M to M 거래기능 API 서버 구현
- Member to Member 회원간 거래를 할 수 있는 기능을 구현하고 있습니다.
- 중고거래 어플 당근 마켓에서 모티브를 얻어 거래 기능 API를 제공하는 서버를 구현하고 있습니다.

## 특징

- msa-project :  gradle 모듈 시스템으로 서비스에 역활과 책임을 나누어 micro-service-architecture 로 구현 해보았습니다.
- xxx-service :  각 모듈은 xxx-service 란 명칭으로 구분하고 있습니다. 각 서비스에는 최소한의 역활 및 책임만 가지도록 하고있습니다.

## 프로젝트 구조

```bash
bori-mairket
├── address-service
├── frm-service
├── security-service
└── trade-service

```

### version 1.0.1
- 2022/11/27

|서비스|구현 기능|
|----|------|
|security-service|회원가입,로그인,요청인증,인가 기능, 내부 API 요청 기능|
|trade-service|판매글 작성, 조회,수정,삭제 기능 구현|
|frm-service|이미지 파일 단건 저장, 여러건 저장 기능 구현|
|address-service|주소 검색 기능 구현|

## API 문서
[API 문서 링크로 보기](http://localhost:8080:/docs/index.html)



## 깃허브
[https://github.com/ImBoriPapa/bori-market](https://github.com/ImBoriPapa/bori-market)