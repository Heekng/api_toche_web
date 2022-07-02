# api_toche_web

## 개요

- `api_toche_web`은 리그오브레전드의 전략적 팀 전투 게임에서 초보들이 현재 가지고 있는 덱을 통해 랭커(마스터, 그랜드마스터, 챌린저)들이 가장 자주 사용하는 최종 덱을 제공하는 서비스입니다.
- 현재 모든 URL에 대한 요청을 허용하고 있으며, 이는 제한될 수 있습니다.

## 개발자

- Backend Developer : [heekng](https://github.com/heekng)
  - [api_toche_web Github](https://github.com/Heekng/api_toche_web)
  - [TOCHE ERD](https://www.erdcloud.com/d/5bCryn3uQmrtpRAkJ)
  - [TOCHE_API WorkFlow](https://github.com/Heekng/api_toche_web/projects/1)
- Frontend Developer : [corner](https://github.com/Eight-Corner)
  - [front_toche_web Github](https://github.com/Eight-Corner/front_toche_web)

## 서비스

- [api_toche_web API Doc](https://documenter.getpostman.com/view/15429365/UzBiPUZe)
- [TFT 초심자 가이드 웹 서비스](https://front-toche-web.vercel.app/)

## api_toche_web에서 제공하는 정보

- Season(시즌)
  - 시즌 리스트 조회
  - 시즌 상세 조회
- Item(아이템)
  - 아이템 리스트 조회
  - 아이템 상세 조회
  - 아이템 최다 사용 TOP5 유닛 조회
- Trait(특성)
  - 특성 리스트 조회
  - 특성 상세 조회
- Augment(증강체)
  - 증강체 리스트 조회
  - 증강체 상세 조회
- Unit(챔피언)
  - 챔피언 리스트 조회
  - 챔피언 상세 조회
  - 챔피언 최다 사용 TOP5 아이템 조회
- Guid(가이드)
  - 챔피언을 통한 최적 덱 조회
  - 증강체를 통한 최적 덱 조회

## 데이터 수집

- 시즌, 챔피언, 증강체, 특성, 아이템에 대한 정보는 [CommunityDragon](https://raw.communitydragon.org/)을 통해 제공받고 있습니다.
  - 하루 한번 새로 업데이트되었는지 여부를 확인하고, 각 항목을 업데이트합니다.
- 랭커, 게임정보는 [Riot API](https://developer.riotgames.com/)에서 제공받고 있습니다.
  - 하루 한번 랭커 리스트를 수집합니다.
  - 2시간 간격으로 교차하여 게임리스트, 게임 상세 정보를 수집합니다.

## 사용 기술

- SpringBoot
- SpringBatch
- Jpa, SpringDataJpa, Querydsl

