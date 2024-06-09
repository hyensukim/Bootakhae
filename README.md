# BooTakHae Application

## 프로젝트 소개
헬스 케어를 위해 건강 식품을 판매하는 E-Commerce 도메인 플랫폼입니다. 특정 이벤트 상품에 대한 대용량의 주문 요청을 정확하고 빠르게 처리하여 사용자 만족도를 향상시키기 위한 서비스 입니다.

<br>

## 주요 기능
- 회원
  - 회원가입
  - 이메일 인증
  - 로그인
  - 로그아웃
- 위시리스트
  - 상품 등록
  - 상품 수량 변경
  - 위시리스트 내 상품 제거
- 주문
  - 주문 등록
  - 주문 취소
  - 반품
  - 회원 주문 내역 조회
- 이벤트
  - 매일 오후 2시 - 3시 이벤트 상품 Open

<br>

## Architecture

![picture](https://raw.githubusercontent.com/hyensukim/Bootakhae/main/images/Architecture(2).png)

<br>

## 기술 스택
- Java 21
- Spring Boot 3.2.5
- Spring Cloud Gateway 4.1.2
- Service Discovery(Eureka Server) 2.0.2
- Resilience4j 2.1.0
- RabbitMQ 3.13.2
- MariaDB 11.3.2
- Redis 7.2.4
- JMeter 5.6.3

<br>

## 기술적 의사결정

- Cache 저장소 위치 : Local vs. Global & Redis vs. Memcached( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Local-vs.-Global-Cache) )
- 동시성 문제 해결 방법 : Pessimistic Lock vs. Reddison
    - 주문 서비스 구현 중 재고 차감 처리 시 동시성 문제 발생( [자세히 보기](https://velog.io/@hyensukim/BootakHae-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0) )
    - 해결 방법 중 Redis를 활용한 분산락 도입( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B02) )
- 웹 애플리케이션 성능 측정을 위해 JMter 사용( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EB%8F%84%EA%B5%AC-%EC%9D%98%EC%82%AC-%EA%B2%B0%EC%A0%95-63ypxm40) )

<br>

## 성능 개선

- Redis Caching을 통한 조회 성능 향상 :  36.22ms → 5.49ms [85% 개선] ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%83%81%ED%92%88-%EB%AA%A9%EB%A1%9D-%EC%BA%90%EC%8B%B1%ED%95%98%EA%B8%B0) )

<br>

## 트러블 슈팅

- wishlist 내 상품 일괄 주문 처리 중 Redisson Lock 사용 시 문제 발생( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%9C%84%EC%8B%9C%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EB%82%B4-%EC%A0%84%EC%B2%B4-%EC%83%81%ED%92%88-%EC%A3%BC%EB%AC%B8-%EC%8B%9C-Lock-%EC%A0%81%EC%9A%A9-%EB%B0%A9%EC%8B%9D) )
- wishlist 상품 일괄 주문 등록 실행 중 오류 발생 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-wishlist-%EC%83%81%ED%92%88-%EC%9D%BC%EA%B4%84-%EC%A3%BC%EB%AC%B8-%EB%93%B1%EB%A1%9D-%EC%8B%A4%ED%96%89-%EC%A4%91-%EC%98%A4%EB%A5%98-%EB%B0%9C%EC%83%9D) )
- Cache와 DB 간의 데이터 불일치 문제 발생 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Redis-Cache%EC%99%80-MariaDB-%EA%B0%84%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%95%ED%95%A9%EC%84%B1-%EB%B6%88%EC%9D%BC%EC%B9%98-%EB%AC%B8%EC%A0%9C-%EB%B0%9C%EC%83%9D-5vqmq14c) )
- Spring Cloud Gateway filter 적용되지 않는 오류 발생 ( [자세히 보기](https://velog.io/@hyensukim/TIL-Spring-Cloud-Gateway-filter-%EC%A0%81%EC%9A%A9%EB%90%98%EC%A7%80-%EC%95%8A%EB%8A%94-%EC%98%A4%EB%A5%98-%EB%B0%9C%EC%83%9D) )

<br>

## 기타 구현

- 이메일 인증을 위한 임시 번호 저장소 변경( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%9D%B8%EC%A6%9D-%EA%B8%B0%EB%8A%A5-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-gj99sx0f) )
- Spring Scheduler + RabbitMQ 활용하여 특정 이벤트 발생
  - Scheduler 도입( [자세히 보기](https://velog.io/@hyensukim/Spring-Scheduler-%EB%8F%84%EC%9E%85%EA%B8%B0) )
  - Schedule-Service 분리 및 RabbitMQ를 활용한 이벤트 발생 ( [자세히 보기](https://velog.io/@hyensukim/BooTakHae-Scheduler-%EA%B4%80%EB%A0%A8-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-g0c6yxzw) )

<br>

## ERD & API 명세서

- [API 명세서](https://dark-elm-e4a.notion.site/3643ce7aec4b4dea82b4c3bc8535a76f?v=ff50dfcedb3b4b27ab999ae160c74d27&pvs=4)

- ERD
    ![ERD](https://raw.githubusercontent.com/hyensukim/Bootakhae/main/images/ERD.png)
