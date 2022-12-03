# frm service

## (File-Resource-Management Service)
- 이미지와 같은 파일을 관리하는 서버입니다. 
- 요청시 전송 받은 파일을 외부 저장소에 저장하고 접근 경로로 이미지파일에 접근할 수 있습니다.

### 응답 예시  
    {
    "success": true,
    "tradeId": 1,
    "imagePath": [
    "http://localhost:8095/file/trade-images/58421c11-5ac8-4721-bd73-c68b9c215731.JPG"
    ]
    }
### 접근 경로 예시 
    "http://localhost:8095/file/trade-images/58421c11-5ac8-4721-bd73-c68b9c215731.JPG"

## ProfileImage

### 1.프로필 이미지 저장 및 경로 생성

#### URI : POST /internal/frm/account/{accountId}/profile
- 프로필 이미지 객체 생성 요청 
- 계정 아이디를 저장한 ProfileImage 객체를 생성 후 기본 프로필 이미지 경로를 반환합니다.

#### Request parameter
- path parameter : accountId -계정 아이디
#### Response parameter
- success : 응답 성공 여부
- accountId : 계정 아이디
- imagePath : 이미지 경로

### 2.프로필 이미지 수정

#### URI : PUT /internal/frm/account/{accountId}/profile
- 프로필 이미지 수정 요청
- 수정할 이미지를 전송하면 수정된 이미지의 경로를 반환 합니다.
- 수정할 이미지를 전송하지 않으면 기본 이미지를 반환 합니다.

#### Request header
- Content-Type : multipart/form-data

#### Request parameter
- path parameter : accountId -계정 아이디
- image :  수정할 프로필 이미지 파일

#### Response parameter
- success : 응답 성공 여부
- accountId : 계정 아이디
- imagePath : 이미지 경로

### 3.프로필 이미지 삭제

#### URI : DELETE /internal/frm/account/{accountId}/profile
- 프로필 이미지 수정 삭제
- 프로필 이미지 객체를 삭제 합니다.

#### Request parameter
- path parameter : accountId -계정 아이디

#### Response parameter
- success : 응답 성공 여부
- accountId : 계정 아이디
- imagePath : null

## TradeImage

### 1.판매 상품 이미지 저장 및 경로 생성

#### URI : POST /internal/frm/trade-image
- 이미지를 저장 후 tradeImageId를 반환 합니다.

#### Request header
- Content-Type : multipart/form-data

#### Request parameter
- images : 저장할 이미지 파일 최대 10개 
#### Response parameter
- success : 응답 성공 여부
- tradeImageId : 계정 아이디
- imagePath[] : 이미지 경로 배열

### 2.판매 상품 이미지 수정

#### URI : PUT /internal/frm/trade-image/{image-id}
- 판매 상품 이미지 수정 요청 저장된 이미지를 삭제하고 새로운 이미지 경로를 반환합니다.

#### Request header
- Content-Type : multipart/form-data

#### Request parameter
- path parameter : tradeImageId -판매 이미지 아이디
- images : 저장할 이미지 파일 최대 10개
#### Response parameter
- success : 응답 성공 여부
- tradeImageId : 판매 이미지 아이디
- imagePath[] : 이미지 경로 배열

### 3.판매 상품 이미지 삭제

#### URI : DELETE /internal/frm/trade-image/{image-id}
- 판매 상품 이미지 삭제 요청 저장된 이미지를 삭제

#### Request parameter
- path parameter : tradeImageId -판매 이미지 아이디

#### Response parameter
- success : 응답 성공 여부
- tradeImageId : 판매 이미지 아이디
- imagePath[] : null