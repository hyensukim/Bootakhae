# BooTakHae Application

## 프로젝트 소개

대용량 주문 요청을 정확하고 빠르게 처리하여 사용자 만족도를 향상시키기 위한 서비스 입니다.

<br>

## 기술 스택

Dev Stack

- Spring Boot
- Spring Data JPA
- Spring Boot Scheduler
- Spring Cloud Gateway
- Service Discovery(Eureka Server)
- Resilience4j
- RabbitMQ

DB 

- MariaDB
- Redis

Test

- JMeter

<br>

## 아키텍처
![picture](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F81cfe124-34c3-47ae-8bfa-2111950b84c6%2F21963d37-df36-47d3-a9f2-34b0b2010992%2F%25EC%25A0%259C%25EB%25AA%25A9_%25EC%2597%2586%25EB%258A%2594_%25EB%258B%25A4%25EC%259D%25B4%25EC%2596%25B4%25EA%25B7%25B8%25EB%259E%25A8.drawio_(1).png?table=block&id=1fa7a446-499c-478d-bbcc-0abcb846a325&spaceId=81cfe124-34c3-47ae-8bfa-2111950b84c6&width=2000&userId=4143463d-d548-47bb-97d8-60f13214f72c&cache=v2)

<br>

## 기술적 의사결정
- Cache 저장소 : Local vs. Global & Redis vs. Memcached

- 동시성 해결 방법 : Pessimistic Lock vs. Redsisson

- CircuitBreaker 및 Retry 도입 개소

<br>

> 자세한 설명은 [여기](https://www.notion.so/BooTakHae-a6b34a94ac284744a8b7c5d8d09edd17?pvs=4#07181ad4525e47f88b1e954f5485cb80) 를 클릭해주세요

<br>

## Trouble Shhoting
- wishlist 내 상품 일괄 주문 처리 중 Redisson Lock 사용 시 문제

- CircuitBreaker 실패율 집계 시 400번 대 에러도 counting 되는 문제

- Spring Cloud Gateway filter가 적용되지 않는 문제

- Redis Cache와 DB 데이터 간에 정합성이 맞지 않는 문제

<br>

> 자세한 설명은 [여기](https://www.notion.so/BooTakHae-a6b34a94ac284744a8b7c5d8d09edd17?pvs=4#0d2cb10b06024055bba438be4e0f39ff) 를 클릭해주세요

<br>

## 기능 개선
- Redis Caching을 통한 조회 성능 향상 : : 36.22ms / tps → 5.49ms / tps

<br>

> 자세한 설명은 [여기](https://www.notion.so/BooTakHae-a6b34a94ac284744a8b7c5d8d09edd17?pvs=4#293c7ae2d9fe4e0ca00db5be51bffb7d) 를 클릭해주세요

<br>

## ERD & API 명세서
- [API 명세서](https://dark-elm-e4a.notion.site/3643ce7aec4b4dea82b4c3bc8535a76f?v=ff50dfcedb3b4b27ab999ae160c74d27&pvs=4)

- ERD
    ![ERD](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F81cfe124-34c3-47ae-8bfa-2111950b84c6%2Fe1a3515f-1080-4d79-b1ab-c4ad01447eef%2FdrawSQL-image-export-2024-05-16.png?table=block&id=4977227b-5063-4b75-9b3b-5df515dd4950&spaceId=81cfe124-34c3-47ae-8bfa-2111950b84c6&width=2000&userId=4143463d-d548-47bb-97d8-60f13214f72c&cache=v2)