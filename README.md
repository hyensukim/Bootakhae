# BooTakHae Application

## 프로젝트 소개

E-Commerce 도메인을 활용하여 대용량 주문 요청을 정확하고 빠륻게 처리하여 사용자 만족도를 향상시키기 위한 서비스 입니다.

<br>

## 프로젝트 목표

- 상품의 재고 처리 시 발생하는 동시성 문제를 완전하게 제어합니다.
- MSA 환경 에서의 부하 분산(Load Balancing) 및 회복 탄력성을 통해 안전한 서버를 구축합니다.
- 대용량 트레픽 처리를 위해 Scale-Out을 통한 로드 밸런싱 및 Cache를 활용합니다.

<br>

## 기술 스택

Dev Stack
- Java 21
- Spring Boot 3.2.5
- Spring Data JPA 3.2.5
- Spring Cloud Gateway 4.1.2
- Service Discovery(Eureka Server) 2.0.2
- Resilience4j 3.1.1
- RabbitMQ 3.13.2

DB 

- MariaDB 11.3.2
- Redis 7.2.4

Test

- JMeter 5.6.3

<br>

## 주요 기능

1. 위시리스트 내 상품 주문하기
    - 재고 처리 관련 동시성 제어 : 비관적 락
    - 상품 서버 Scale-Out 및 로드 밸런싱
2. 시스템 안정성
    - Resilience4j - CircuitBreaker + Retry
3. 상품 목록 조회
    - Redis Cache
4. 특정 시간동안 상품 이벤트 진행
    - Spring Scheduler
    - RabbitMQ

<br>

## Architecture

![picture](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F81cfe124-34c3-47ae-8bfa-2111950b84c6%2F21963d37-df36-47d3-a9f2-34b0b2010992%2F%25EC%25A0%259C%25EB%25AA%25A9_%25EC%2597%2586%25EB%258A%2594_%25EB%258B%25A4%25EC%259D%25B4%25EC%2596%25B4%25EA%25B7%25B8%25EB%259E%25A8.drawio_(1).png?table=block&id=1fa7a446-499c-478d-bbcc-0abcb846a325&spaceId=81cfe124-34c3-47ae-8bfa-2111950b84c6&width=2000&userId=4143463d-d548-47bb-97d8-60f13214f72c&cache=v2)

<br>

## 기술적 의사결정

- Cache 저장소 위치 : Local vs. Global & Redis vs. Memcached( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Local-vs.-Global-Cache) )
- 동시성 문제 해결 방법 : Pessimistic Lock vs. Reddison
    - 주문 서비스 구현 중 재고 차감 처리 시 동시성 문제 발생( [자세히 보기](https://velog.io/@hyensukim/BootakHae-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0) )
    - 해결 방법 중 Redis를 활용한 분산락 도입( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B02) )
- CircuitBreaker 및 Retry 도입 개소
    - 이벤트 시간 동안 주문 처리 로직에 트레픽이 응집될 것을 대비하여 도입( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-CircuitBreaker-%EB%8F%84%EC%9E%85-%EA%B0%9C%EC%86%8C) )

<br>

## 성능 개선

- Redis Caching을 통한 조회 성능 향상 :  36.22ms → 5.49ms [85% 개선] ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%83%81%ED%92%88-%EB%AA%A9%EB%A1%9D-%EC%BA%90%EC%8B%B1%ED%95%98%EA%B8%B0) )

<br>

## 트러블 슈팅

- wishlist 내 상품 일괄 주문 처리 중 Redisson Lock 사용 시 문제 발생( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%9C%84%EC%8B%9C%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EB%82%B4-%EC%A0%84%EC%B2%B4-%EC%83%81%ED%92%88-%EC%A3%BC%EB%AC%B8-%EC%8B%9C-Lock-%EC%A0%81%EC%9A%A9-%EB%B0%A9%EC%8B%9D) )
- wishlist 상품 일괄 주문 등록 실행 중 오류 발생 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-wishlist-%EC%83%81%ED%92%88-%EC%9D%BC%EA%B4%84-%EC%A3%BC%EB%AC%B8-%EB%93%B1%EB%A1%9D-%EC%8B%A4%ED%96%89-%EC%A4%91-%EC%98%A4%EB%A5%98-%EB%B0%9C%EC%83%9D) )
- Cache와 DB 간의 데이터 불일치 문제 발생 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Redis-Cache%EC%99%80-MariaDB-%EA%B0%84%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%95%ED%95%A9%EC%84%B1-%EB%B6%88%EC%9D%BC%EC%B9%98-%EB%AC%B8%EC%A0%9C-%EB%B0%9C%EC%83%9D-5vqmq14c) )
- Spring Cloud Gateway filter 적용되지 않는 오류 발생 ( [자세히 보기](https://velog.io/@hyensukim/TIL-Spring-Cloud-Gateway-filter-%EC%A0%81%EC%9A%A9%EB%90%98%EC%A7%80-%EC%95%8A%EB%8A%94-%EC%98%A4%EB%A5%98-%EB%B0%9C%EC%83%9D) )
- CircuitBreaker 실패율 집계 시 400번대 오류도 카운팅되는 오류 발생 ( [자세히 보기](https://velog.io/@hyensukim/TIL-CircuitBreaker-%EC%8B%A4%ED%8C%A8%EC%9C%A8-%EC%A7%91%EA%B3%84-%EC%8B%9C-400%EB%B2%88%EB%8C%80-%EC%98%A4%EB%A5%98%EB%8F%84-%EC%B9%B4%EC%9A%B4%ED%8C%85%EB%90%98%EB%8A%94-%EC%98%A4%EB%A5%98-%EB%B0%9C%EC%83%9D) )

<br>

## 기타 구현

1. 이메일 인증을 위한 임시 번호 저장소 변경
2. Spring Scheduler + RabbitMQ 활용하여 특정 이벤트 발생
    1. Scheduler 도입( [자세히 보기](https://velog.io/@hyensukim/Spring-Scheduler-%EB%8F%84%EC%9E%85%EA%B8%B0) )
    2. Schedule-Service 분리 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Scheduler-%EA%B4%80%EB%A0%A8-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-g0c6yxzw) )
    3. RabbitMQ를 활용한 이벤트 발생 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Schedule-Service%EC%97%90-RabbitMQ-%EB%8F%84%EC%9E%85-lmm2a19s) )

<br>

## ERD & API 명세서

- [API 명세서](https://dark-elm-e4a.notion.site/3643ce7aec4b4dea82b4c3bc8535a76f?v=ff50dfcedb3b4b27ab999ae160c74d27&pvs=4)

- ERD
    ![ERD](https://raw.githubusercontent.com/hyensukim/Bootakhae/main/images/ERD.png)
