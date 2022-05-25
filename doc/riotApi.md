# Riot API used guid

## 사용자 정보 받아오기

- TFT 사용자 정보를 사용자 계정 이름으로 받아온다.
- `https://kr.api.riotgames.com`
- `/tft/summoner/v1/summoners/by-name/{summonerName}`
- Request
  - summonerName: 사용자 닉네임
- Response
  - accountId: string, 암호화된 계정 ID, 56자
  - profileIconId: int, 소환사 아이콘 ID
  - revisionDate: long, 소환사가 마지막으로 수정된 날짜
  - name: string, 소환사 이름
  - id: string, 암호화된 소환사 ID, 63자
  - puuid: string, 암호화된 PUUID, 78자
  - summornerLevel: long, 소환사 레벨

## 사용자 최근 게임 리스트 받아오기

- 사용자 PUUID를 이용해 최근 게임 리스트를 받아온다.
- `https://{region}.api.riotgames.com`
- `/tft/match/v1/matches/by-puuid/{puuid}/ids`
- region
  - asia
  - europe
  - americas
- Request
  - start: int, 인덱스 시작점, 기본값 0
  - endTime: long, Epoch 타임스탬프(초)
  - startTime: long, Epoch 타임스탬프(초)
  - count: int, 반환할 일치 ID 수, 기본값 20
- Response
  - matchId: List<String>, 게임 고유번호

## 게임정보 받아오기

- matchId로 해당 게임의 정보를 받아온다.
- `https://{region}.api.riotgames.com`
- `/tft/match/v1/matches/{matchId}`
- request
  - matchId: 게임 고유번호
- response
  - metadataDto
    - data_version: string, 데이터버전 일치
    - match_id: string, 일치된 ID
    - participants: List<string>, 참가자 PUUID 리스트
  - infoDto
    - game_datetime: long, 유닉스 타임스탬프
    - game_length: float, 게임 길이(초)
    - game_variation: string, 게임 변형 키. TFT 정적 데이터에 문서화된 게임 변형
    - game_version: string, 게임 클라이언트 버전
    - participants: List<ParticipantDto>, 참가자 리스트 
      - companion: CompanionDto, 참가자
        - content_ID: 
        - skin_ID: 
        - species: 
      - gold_left: int, 마지막으로 남은 골드
      - last_round: int, 마지막 라운드
      - level: int, 마지막 레벨
      - placement: int, 마지막 참가자 배치
      - players_eliminated: int, 참가자가 제거한 플레이어 수
      - puuid: string
      - time_eliminated: float, 참가자 플레이 시간
      - total_damage_to_players: int, 다른 참가자에게 입힌 피해
      - traits: List<TraitDto>, 참가자 활성 유닛에 대한 전체 특성 목록
        - name: string, 특성 이름
          - `시즌`_`특성명`
        - num_units: int, 특성 유닛 수
        - style: int, 특성 스타일(등급)
          - 0: 스타일 없음
          - 1: 브론즈
          - 2: 실버
          - 3: 골드
          - 4: 유채색
        - tier_current: int, 특성에 대한 현재 활성 계층
        - tier_total: int, 특성 총 계층
      - units: List<UnitDto>, 참가자의 활성 단위 목록
        - items: List<int>, 아이템 목록
        - character_id: string, 캐릭터 아이디
        - chosen: string, 
        - name: string, 유닛명
        - rarity: int, 유닛 희귀도
        - tier: int, 유닛 티어
    - queue_id: int
    - tft_game_type: int, 
    - tft_set_number: int, TFT 세트 번호

## 랭커 리스트

- 호출시점의 랭커 리스트를 받아온다.
- `https://kr.api.riotgames.com`
- `/tft/league/v1/challenger`
- Response
    - tier: string
    - leagueId: string
    - queue: string
    - name: string
    - entries: List<LeagueItemDto>
        - freshBlood: boolean
        - wins: int, 1등 횟수
        - summonerName: string
        - miniSeries: MiniSeriesDTO
            - losses: int
            - progress: string
            - target: int
            - wins: int
        - inactive: boolean
        - veteran: boolean
        - hotStreak: boolean
        - rank: string
        - leaguePoints: int
        - losses: int, 1등이 아닌 경우
        - summornerId: int