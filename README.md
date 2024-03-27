# High-volume_traffic
- 대용량 트래픽 처리를 위한 백엔드 심화 연습

- 대용량 트래픽 처리를 위한 방안으로 사용되는 기술에 대해서 알아보기.
- KTX 설날 예매, 블랙프라이데이 이벤트 대규모 트래픽이 발생할 수 있는 상황에서 어떻게 대처하면 좋을지 생각해보기.
- 대기열 시스템을 구현해보면서 알아보기.
  
## In-memory Database Redis
- NoSQL 로 Key-Value 형태의 DB 인 Redis 의 다양한 타입에 대해서 알아보았다.
  - String, List, Set, Geospatial ...

- Geospatial Type 을 이용하여 주변의 카페 정보를 나타낼 수 있는 간단한 프로그램 구현
  - CafeAroundMe ( Html geolocation 으로 현재 위치를 찾아내고 주변 카페 정보를 나타내는 지도 웹 애플리케이션 )
  - 사용 기술
    - SpringBoot3.2.4
    - Java17
    - Redis
    - Swagger
    - HTML
    - Kakao Open API 를 활용
      - 카테고리로 장소 검색하기 ( https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category )
      - 카카오 지도 ( https://apis.map.kakao.com/web/ )
  - 동작 흐름
    
    <img width="788" alt="image" src="https://github.com/IMWoo94/High-volume_traffic/assets/75981576/431453a6-cb65-4985-a6ca-ca3288327b0f">

  - 동작 화면
    
    <img width="889" alt="image" src="https://github.com/IMWoo94/High-volume_traffic/assets/75981576/23600a82-31e2-45aa-a5d7-615d07114873">

## Reactive Programming Spring Webflux

