# 금방주식회사 백엔드 기업과제_FridayMarket
금을 판매하고 구매하는 웹 서비스입니다.

## 🚀 도전 과제

금을 한창 열심히 팔고 있던 알레테이아는, 금을 판매하고 구매하는 서비스를 제공하기로 결정했습니다!
알레테이아는 앱을 통해 구매, 판매 주문을 관리하려고 합니다! 또한 미래에 서비스가 확장될 것을 고려하여,
인증을 담당하는 서버를 별도로 구축하기로 결정했는데요,
과연 어떻게 해야 잘 만들 수 있을까요?

## ✍️ 메인 과제 목표

1. RESTful API를 활용하여 구매, 판매 주문 CRUD를 수행하는 서버 A 구현
2. 서버 A와 grpc를 통해 소통하며, 인증만을 담당하는 서버 B 구현


## Quick Start

``` 
git clone https://github.com/ohyeryung/FridayMarket.git 
```

```
docker-compose -f ./auth-server/docker-compose-auth.yml up -d

docker-compose -f ./resource-server/docker-compose-resource.yml up -d
```

## ERD

인증서버 ERD

<br>

자원서버 ERD


<br>

## API

인증서버 API

<br>

자원서버 API

## 트러블슈팅

