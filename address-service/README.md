# address-service

## 주소 검색 API

- bori-market 프로젝트에서 필요한 형태의 주소를 조회할 수 있는 service입니다.
- 회원가입시 사용해야할 주소 데이터 양식으로 데이터를 제공합니다.

## API
### 요청
#### URL: GET /address - 주소 전체를 조회합니다. 
#### URL: GET /address?town=삼성동 - 동네 이름으로 주소를 검색합니다.

### 응답 아래와 같은 데이터가 응답됩니다.

    http://localhost:8085/address?town=삼성

    HTTP/1.1 200 OK
    transfer-encoding: chunked
    Content-Type: application/json

    [ {
    "city": "서울시",
    "district": "강남구",
    "town": "삼성제1동",
    "addressCode": 1005
    },
    {
    "city": "서울시",
    "district": "강남구",
    "town": "삼성제2동",
    "addressCode": 1006
    } ]

## 검색창
### 요청
#### URL: GET /address/search-form - 주소를 검색할 수 있는 form을 제공합니다.

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
검색 기능
<form class="form">
    <input type="text" name="town" id="town" placeholder="검색할 동네를 입력하세요">
    <button type="submit">검색</button>
</form>

<p class="contents"></p>

</body>

</html>

    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
    검색 기능
    <form class="form">
        <input type="text" name="town" id="town" placeholder="검색할 동네를 입력하세요">
        <button type="submit">검색</button>
    </form>
    
    <p class="contents"></p>
    
    </body>
    
    <script>
        const form = document.querySelector('.form');
        const contents = document.querySelector('.contents');
        const keyword = document.getElementById("town");
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            const value = keyword.value;
            fetch(`/address?town=${value}`)
                .then((resonse) => resonse.json())
                .then(renderContents)
        });
    
        function renderContents(data) {
            contents.textContent = JSON.stringify(data);
        }
    
    
    </script>
    
    </html>
### 폼에서 검색하여도 같은 형태의 데이터를 응답받을 수 있습니다.
