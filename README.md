# 금방주식회사 백엔드 기업과제_FridayMarket
금을 판매하고 구매하는 웹 서비스입니다.

<br>

## 🚀 도전 과제

금을 한창 열심히 팔고 있던 알레테이아는, 금을 판매하고 구매하는 서비스를 제공하기로 결정했습니다!
알레테이아는 앱을 통해 구매, 판매 주문을 관리하려고 합니다! 또한 미래에 서비스가 확장될 것을 고려하여,
인증을 담당하는 서버를 별도로 구축하기로 결정했는데요,
과연 어떻게 해야 잘 만들 수 있을까요?

<br>

## 🛠️ 프로젝트 환경

| Stack                                                                                                        | Version           |
|:------------------------------------------------------------------------------------------------------------:|:-----------------:|
| ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) | Spring Boot 3.3.x |
| ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)    | Gradle 8.8       |
| ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)    | JDK 17           |
| ![MySQL](https://img.shields.io/badge/mariaDB-4479A1.svg?style=for-the-badge&logo=mariaDB&logoColor=white)   | MariaDB 11.5     |
| ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)    | Redis 6.0        |

<br>

## Quick Start

1. 해당 프로젝트를 clone 받아주세요.
``` 
git clone https://github.com/ohyeryung/FridayMarket.git 
```

2. Docker 및 Docker Compose가 설치되어 있어야 합니다. (버전 20.10 이상 권장)
3. 데이터베이스 실행
애플리케이션을 시작하기 전에 데이터베이스를 Docker Compose를 사용하여 설정해야 합니다. 다음 명령어를 사용하여 각 서버의 데이터베이스를 실행합니다.

```
docker-compose -f ./auth-server/docker-compose-auth.yml up -d

docker-compose -f ./resource-server/docker-compose-resource.yml up -d
```

4. Redis 패스워드 설정
```
1) docker exec -it [CONTAINER ID] redis-cli
2) config get requirepass
3) config set requirepass [PASSWORD]
4) config get requirepass
5) exit
```
5. .env 파일 설정
    <details>
        <summary><b>환경설정 세부내용</b></summary><br>
      
         .env.example과 같이 값을 설정합니다.
       인증서버 .env
       ```
        // DB 관련 설정
        DB_HOST=${DB_HOST}
        DB_PORT=${DB_PORT}
        DB_CONTAINER_NAME=${DB_CONTAINER_NAME}
        DB_NAME=${DB_NAME}
        DB_USER=${DB_USER}
        DB_PASSWORD=${DB_PASSWORD}
        
        // JWT 관련 설정
        JWT_SECRET_KEY=${JWT_SECRET_KEY}
        JWT_ISSUER=${JWT_ISSUER}
        ACCESS_TOKEN_VALID_MILLI_SEC=${ACCESS_TOKEN_VALID_MILLI_SEC}
        REFRESH_TOKEN_VALID_MILLI_SEC=${REFRESH_TOKEN_VALID_MILLI_SEC}
        
        CLAIM_USERNAME=${CLAIM_USERNAME}
        
        // REDIS 관련 설정
        REDIS_HOST=${REDIS_HOST}
        REDIS_PORT=${REDIS_PORT}
        REDIS_CONTAINER_NAME=${REDIS_CONTAINER_NAME}
        REDIS_PASSWORD=${REDIS_PASSWORD}
        
        // 서버 포트 설정
        SERVER_PORT=${SERVER_PORT}
        
        // gRPC 서버 포트 설정
        GRPC_SERVER_PORT=${GRPC_SERVER_PORT}
       ```
    
    <br>
    
       자원서버 .env
       ```
           // DB 관련 설정
        DB_HOST=${DB_HOST}
        DB_PORT=${DB_PORT}
        DB_CONTAINER_NAME=${DB_CONTAINER_NAME}
        DB_NAME=${DB_NAME}
        DB_USER=${DB_USER}
        DB_PASSWORD=${DB_PASSWORD}
        
        // 서버 포트 설정
        SERVER_PORT=${SERVER_PORT}
        
        // gRPC 관련 설정
        GRPC_HOST=${GRPC_HOST}
        
        GRPC_AUTH_SERVER_PORT=${GRPC_AUTH_SERVER_PORT}
        
        GRPC_RESOURCE_SERVER_PORT=${GRPC_RESOURCE_SERVER_PORT}
       ```
    
    </details>
  
## ERD

인증서버 ERD

![FridayMarket__Auth_ERD_최최종](https://github.com/user-attachments/assets/287fcd24-a623-4f7f-9429-f751393c0817)

<br>

자원서버 ERD
![FridayMarket_Resource](https://github.com/user-attachments/assets/07e5b6b2-0e44-48f9-8f8b-bfa7d765e005)


<br>

## API [Postman API](https://documenter.getpostman.com/view/20456478/2sAXqmB5s5)

<img width="954" alt="인증서버 API 명세" src="https://github.com/user-attachments/assets/501ee75c-51c7-4e36-bb9b-50c48fc80782">

<br>


<img width="1174" alt="자원서버 API 명세" src="https://github.com/user-attachments/assets/76b59ede-7f7d-49ac-bef0-d7fde0897da9">

<br>

## 트러블슈팅

#### 회원가입 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/5)
#### 로그인 기능 구현 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/9)
#### AccessToken 재발급 테스트 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/24)
#### 로그아웃 기능 구현 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/26)
#### gRPC 통신 구현 중 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/28)

