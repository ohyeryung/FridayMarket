## 목차

> 1. [서비스 소개](#-서비스-소개)
> 2. [프로젝트 환경](#%EF%B8%8F-프로젝트-환경)
> 3. [주요 기능](주요-기능)
> 4. [Quick Start](#quick-start)
> 5. [ERD](#%EF%B8%8F-erd)
> 6. [API 명세서](#-api-postman-api)
> 7. [트러블슈팅](#-트러블슈팅)


<br/>

## 💰 서비스 소개
게시된 금 상품을 보고 사용자가 구매 또는 판매할 수 있는 온라인 금은방 웹 서비스입니다.
```MSA 관점```에서 유저를 생성하고 토큰을 검증하는 인증 서버와 실제 비즈니스 로직이 구현되는 자원 서버로 분리하여 개발하였습니다.
각 서버 간 통신 방법으로는 ```gRPC 통신 방법```을 사용하였습니다.

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

## 주요 기능

1. MSA 구조 구현
인증 서버와 자원 서버를 분리하여 독립적인 확장과 유지보수가 용이하도록 설계하였습니다.

2. gRPC 통신
고성능 양방향 통신을 위해 gRPC를 사용하여 서버 간의 효율적인 통신을 구현하였습니다.

3. JWT 인증/인가
서버 확장성과 성능 측면에서 유리한 JWT를 기반으로, accessToken과 refreshToken을 발급하여 인증 및 인가를 처리합니다.

4. 로그아웃 처리
accessToken의 만료 시간을 API 호출 시점으로 초기화하고, Redis에 저장된 refreshToken을 삭제하여 토큰을 무력화함으로써 보안을 강화하였습니다.

5. 주문 기능
유저는 등록된 상품에 대해 구매 주문과 판매 주문을 등록할 수 있습니다.

6. 주문 상태 관리
주문의 타입(구매, 판매)에 따라 상태값을 변경할 수 있습니다.

    구매 주문 상태

    - 주문 완료: 구매자가 주문을 완료한 상태.
    - 입금 완료: 구매자의 결제가 확인된 상태.
    - 발송 완료: 판매자가 구매자에게 상품을 발송한 상태.
      
    판매 주문 상태

    - 주문 완료: 구매자가 주문을 완료한 상태.
    - 수령 완료: 검수 센터에서 상품을 수령한 상태.
    - 송금 완료: 검수 센터에서 상품 검수가 완료되어 판매자에게 송금된 상태.

7. 주문 조회
본인이 등록한 주문에 한하여 주문 목록 조회 및 상세 조회가 가능합니다.

<br>

> 주문 상태 관리
> 
> 각각의 주문 상태를 통해 주문 진행 상황을 명확하게 추적하고 관리할 수 있습니다.

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
  
<br>

## ⛓️ ERD

인증서버 ERD

![FridayMarket__Auth_ERD_최최종](https://github.com/user-attachments/assets/287fcd24-a623-4f7f-9429-f751393c0817)

<br>

자원서버 ERD
![FridayMarket_Resource](https://github.com/user-attachments/assets/07e5b6b2-0e44-48f9-8f8b-bfa7d765e005)


<br>

## 📋 API [Postman API](https://documenter.getpostman.com/view/20456478/2sAXqmB5s5)

<img width="954" alt="인증서버 API 명세" src="https://github.com/user-attachments/assets/501ee75c-51c7-4e36-bb9b-50c48fc80782">

<br>


<img width="1174" alt="자원서버 API 명세" src="https://github.com/user-attachments/assets/76b59ede-7f7d-49ac-bef0-d7fde0897da9">

<br>

## 💥 트러블슈팅

#### 회원가입 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/5)
#### 로그인 기능 구현 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/9)
#### AccessToken 재발급 테스트 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/24)
#### 로그아웃 기능 구현 시 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/26)
#### gRPC 통신 구현 중 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/28)
#### 주문 내역 목록 조회 기능 구현 중 발생한 오류 (https://github.com/ohyeryung/FridayMarket/pull/47)

