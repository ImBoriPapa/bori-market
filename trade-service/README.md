# trade-service

***

## 판매글 게시판 서버입니다.

- security-service 에서 요청받은 정보를 처리또는 제공하는 내부 API 를 제공합니다.
- 판매 글을 작성,조회,수정,삭제 API 를 제공합니다.
- frm-service 에 상품이미지와 같은 이미지 파일 저장 및 관리역활을 위임합니다.

## API 명세

| API 종류 |API| 설명 |
|---|---|---|
|판매 글 생성|POST /internal/trade|판매 글을 생성합니다.|
|판매 글 조회|GET /internal/trade| 판매 글 리스트를 조회합니다.|
|판매 글 내용조회|GET /internal/trade/{tradeId}|판매글의 아이디로 내용을 조회합니다.|
|판매 글 수정|PATCH /internal/trade/{tradeId}|판매글의 내용을 수정할 수 있습니다.|
|판매 글 삭제|DELETE /internal/trade/{tradeId}|한 건의 판매글을 삭제할수 있습니다.|

### 1. 판매 글 생성 POST " /internal/trade "

#### 요청 필드

| 필드        | 타입                  | 설명       | 필수 여부 |
|-----------|---------------------|----------|-------|
| accountId | Long                | 계정 아이디   | true  |
| title     | String              | 판매 글 제목  |true  |
| context   | String              | 판매글 내용   |true  |
| price     | Integer             | 가격       |true  |
| address   | Object              | 계정 아이디   |true  |
| category  | enum                | 카테고리     |true  |
| isShare   | Boolean             | 나눔 여부    |true  |
| isOffer   | Boolean             | 가격 제안 여부 |true  |
| images    | List\<MultipartFile> | 이미지 파일   |true  |

#### 응답 필드

| 필드 | 타입                  | 설명        | 필수 여부 |
|----|---------------------|-----------|-------|
| tradeId | Long                | 판매 글 아이디  | true  |
|  createdAt  | LocalDateTime              | 판매 글 생성시간 |true  |

### 2. 판매 글 리스트 조회 GET " /internal/trade "

#### 요청 파라미터

| 필드        | 타입      | 설명                | 필수 여부 | 기본값  |
|-----------|---------|-------------------|-------|------|
| size | int     | 요청당 조회 사이즈        | false | 10   |
| lastIndex     | Long    | 조회된 마지막 인덱스       | true  | 0    |
| category   | enum    | 검색 조건에 카테고리 추가    | false  | null |
| isShare     | Boolean | 검색 조건에 나눔여부 추가    | false  | null |
| isOffer   | Boolean | 검색 조건에 가격제안 여부 추가 | false  | null |
| status  | enum    | 검색 조건에 판매 글 상태 추가 | false  | SALE |
| addressCode   | Integer | 주소 코드             | true  |      |
| range   | enum    | 주소 검색 범위          | true  |      |

- size 는 입력하지 않으면 디폴트값 10건으로 페이징 검색됩니다.
- lastIndex 입력하지 않으면 0번 인덱스로 조회합니다 다음 페이지를 확인하기 위해서는 조회된 마지막 인덱스를 입력하여야 합니다.
- category [DIGITAL,FURNITURE,BOOK] 과 같은 카테고리 를 검색조건에 추가할 수 있습니다.
- isShare 조건 true 이면 나눔 글만 조회할 수 있습니다.
- isOffer 조건 true 이면 가격제안 허용 글만 조회할 수 있습니다.
- status 조건은 기본은 응답 필드 SALE 판매중인 글을 보여주며 TRADING 거래중, SOLD_OUT 판매 완료 상태에 글을 조회할수 있습니다.
- addressCode 주소코드가 없을 시 검색 결과를 응답할 수 없습니다.
- range 주소 검색 범위가 없을시 검색 결과를 응답할 수 없습니다.

#### 응답 필드(1)

| 필드 | 타입                  | 설명           |
|----|---------------------|--------------|
| size | Integer             | 조회된 사이즈      |
|  hasNext  | Boolean             | 다음 페이지 존재 여부 |
|  result  | List\<TradeListDto> | 결과 리스트       |

#### 응답 필드(2) TradeListDto

| 필드 | 타입            | 설명        |
|----|---------------|-----------|
| tradeId | Long          | 판매글 아이디   |
|  title  | String        | 제목        |
|  address  | String        | 주소        |
|  price  | int           | 가격        |
|  representativeImage  | String        | 대표 이미지 경로 |
|  createdAt  | LocalDateTime | 생성 시간     |
