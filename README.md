# BooTakHae Application

## 프로젝트 소개

이커머스 도메인을 활용하여 대용량의 주문 요청을 정확하고 빠르게 처리하여 사용자 만족도를 향상시키기 위한 서비스 입니다.

<br>

## 프로젝트 목표

- 상품의 재고 처리 시 발생하는 동시성 문제를 완전하게 제어합니다.
- MSA 환경 에서의 부하 분산(Load Balancing) 및 회복 탄력성을 통해 안전한 서버를 구축합니다.
- 상품 조회 및 주문 처리에 빠른 처리가 가능하도록 서버를 설계합니다.

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

## 주요 기능

1. 위시리스트 내 상품 주문하기
    - 재고 처리 관련 동시성 제어 : 비관적 락
2. 시스템 안정성
    - Resilience4j - CircuitBreaker + Retry
3. 상품 목록 조회
    - Redis Cache
4. 특정 시간동안 상품 이벤트 진행
    - Spring Scheduler
    - RabbitMQ

> 추가 예정
- 재고 처리 성능 개선 : Redis
- 재고 복구 이벤트 시 RabbitMQ 사용
- 상품 검색 기능 구현 + 인덱스로 인한 쿼리 개선
- 로드 밸런싱 적용하기

<br>

## Architecture

![picture](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F81cfe124-34c3-47ae-8bfa-2111950b84c6%2F21963d37-df36-47d3-a9f2-34b0b2010992%2F%25EC%25A0%259C%25EB%25AA%25A9_%25EC%2597%2586%25EB%258A%2594_%25EB%258B%25A4%25EC%259D%25B4%25EC%2596%25B4%25EA%25B7%25B8%25EB%259E%25A8.drawio_(1).png?table=block&id=1fa7a446-499c-478d-bbcc-0abcb846a325&spaceId=81cfe124-34c3-47ae-8bfa-2111950b84c6&width=2000&userId=4143463d-d548-47bb-97d8-60f13214f72c&cache=v2)

<br>

## 기술적 의사결정

- Cache 저장소 위치 : Local vs. Global & Redis vs. Memcached
    - 서버 간의 데이터 일관성 유지를 위해서 Global Cache 선택
    - 읽기 성능이 더 우수하며 활용도가 높은 Redis 선택( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Local-vs.-Global-Cache) )
- 동시성 문제 해결 방법 : Pessimistic Lock vs. Reddison
    - 주문 서비스 구현 중 재고 차감 처리 시 동시성 문제 발생( [자세히 보기](https://velog.io/@hyensukim/BootakHae-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0) )
    - 해결 방법 중 Redis를 활용한 분산락 도입( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B02) )
- CircuitBreaker 및 Retry 도입 개소
    - 이벤트 시간 동안 주문 처리 로직에 트레픽이 응집될 것을 대비하여 도입( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-CircuitBreaker-%EB%8F%84%EC%9E%85-%EA%B0%9C%EC%86%8C) )

<br>

## 성능 개선

- Redis Caching을 통한 조회 성능 향상 :  36.22ms / tps → 5.49ms / tps ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%83%81%ED%92%88-%EB%AA%A9%EB%A1%9D-%EC%BA%90%EC%8B%B1%ED%95%98%EA%B8%B0) )

<br>

## 트러블 슈팅

- wishlist 내 상품 일괄 주문 처리 중 Redisson Lock 사용 시 문제 발생
    - 전체 상품 마다 Lock을 걸어주면서 병목 현상이 발생하여 성능 저하 발생( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%9C%84%EC%8B%9C%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EB%82%B4-%EC%A0%84%EC%B2%B4-%EC%83%81%ED%92%88-%EC%A3%BC%EB%AC%B8-%EC%8B%9C-Lock-%EC%A0%81%EC%9A%A9-%EB%B0%A9%EC%8B%9D) )
- Redis Cache와 MariaDB 간의 데이터 정합성 불일치 문제 발생
- CircuitBreaker 실패율 집계 시 400번대 오류도 카운팅되는 오류 발생
- HttpMediaTypeNotAcceptableException 발생
- JPA Detached Entity passed to persist 예외 발생
- Spring Cloud Gateway filter 적용되지 않는 오류 발생

<br>

## 기타 구현

1. 이메일 인증을 위한 임시 번호 저장소 변경
2. Spring Scheduler + RabbitMQ 활용하여 특정 이벤트 발생
    1. Scheduler 도입( [자세히 보기](https://velog.io/@hyensukim/Spring-Scheduler-%EB%8F%84%EC%9E%85%EA%B8%B0) )
    2. Schedule-Service 분리
    3. RabbitMQ를 활용한 이벤트 발생

<br>

## ERD & API 명세서

- [API 명세서](https://dark-elm-e4a.notion.site/3643ce7aec4b4dea82b4c3bc8535a76f?v=ff50dfcedb3b4b27ab999ae160c74d27&pvs=4)

- ERD
    ![ERD](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F81cfe124-34c3-47ae-8bfa-2111950b84c6%2Fe1a3515f-1080-4d79-b1ab-c4ad01447eef%2FdrawSQL-image-export-2024-05-16.png?table=block&id=4977227b-5063-4b75-9b3b-5df515dd4950&spaceId=81cfe124-34c3-47ae-8bfa-2111950b84c6&width=2000&userId=4143463d-d548-47bb-97d8-60f13214f72c&cache=v2)
