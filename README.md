![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

# HideAndSeek Plugin (with ModelEngineAPI)

마인크래프트 서버에서 ModelEngine API를 활용해 플레이어 모델 애니메이션을 관리하는 플러그인입니다.  
아이템을과 명령어를 통해 애니메이션을 실행할 수 있습니다.

---

## 추가

<details>
<summary>v4 예정</summary>
<p></p>

```
Add
- 기존 Shift + F -> gui 변경 예정
- gui 각도 고정기능 추가
- 몹 소환 후 원격 애니메이션 실행
- 관전으로 자신의 모델을 볼수있는 기능

unDisguise
- 변신 해제시 투명, 애니메이션 중지

Skript
- set player's model to (ModelId) 추가 예정
- play auto animation of player to "애니메이션이름"
- play switch animation of player to "애니메이션이름"
- stop animation of player to "애니메이션이름"
```

</details>


<details>
<summary>v3</summary>
<p></p>

```
2025-08-12 1.21.v31

Disguise
- 변신중 다른모델 변신시 undisguise후 disguise

Add
- 1인칭이 가려지는 현상 때문에 Shift + F (On/OFF) 자신에게만 모델이 보이게 안보이게 가능 [ 다른사람은 정상적으로 잘 보임]
- has random 추가 gui에서 원하는 모델 선택후 철주괴 클릭시 아이템 지급, 아이템 우클릭시 로어에 있는 모델중 랜덤으로 소환 타입은 Data.yml에서 변경 가능

Item
- 버리기 방지
- 아이템 이동가능(번호키)
```

</details>

---

## 주요 기능

- 플레이어별 모델 애니메이션 실행 및 중지 제어  
- 아이템(IRON_NUGGET) 클릭 시 애니메이션 실행 또는 중지  
- 애니메이션 타입에 따른 동작 방식 (auto / switch)
- 랜덤으로 모델을 소환 할수 있는 기능 (v3)
- 간단한 커맨드

---

## 명령어 및 설명

| 명령어                                | 설명                                 |
|-------------------------------------|------------------------------------|
| `/hideandseek reload`                | 플러그인 설정 및 리소스 다시 불러오기         |
| `/hideandseek info`                  | 플러그인 정보 및 상태 확인                   |
| `/hideandseek random`                  | 모델 랜덤소환 기능                   |
| `/hideandseek disguise (ModelId) (Player)`    | 특정 플레이어에게 지정한 모델로 변신시키기       |
| `/hideandseek undisguise (Player)`               | 특정 플레이어의 변신 해제                      |
| `/hideandseek play (Player) (AnimationName)`  | 애니메이션 Play |
| `/hideandseek stop (Player) (AnimationName)`  | 애니메이션 Stop |
| `/hideandseek setting (ModelId) (Key) (Value)`  | 모델별 설정 값 변경 (예: 애니메이션 타입, 크기 등) |


---

## Data.yml 기본 구성 예시

```yaml
PlayerScale: 0.5        # 플레이어 크기 조절 (기본 1.0)
ModelScale: 0.5         # 모델 크기 조절 (기본 1.0)
HitBoxScale : 1        # 모델 히트박스 크기 조절 (기본 1.0)
PlayerHealth: 20        # 플레이어 기본 체력 설정 (기본 20)
Animation:
  death: switch         # 애니메이션 타입 설정 (auto / switch) (자동 설정)
```

---

# 애니메이션 타입 변경 방법 (auto ↔ switch)

## Switch 변경방법

1. 먼저 모델을 블록벤치로 열고

![image](https://github.com/user-attachments/assets/760590b6-2f70-4d32-9f76-ed91da71773a)

2. 애니메이션을 들어간다음

![image](https://github.com/user-attachments/assets/431c8345-ede3-44e8-b635-3dc7186d01f4)

3. 애니메이션 더블 클릭 후 루프 만들기를 `마지막 프레임 유지` 로 바꾸기

![image](https://github.com/user-attachments/assets/47918ab3-d899-469e-80af-d9fe0857fd51)

4. 저장 후 서버에서 `hideandseek reload` 입력하면 Data.yml에 변경한 애니메이션이 `switch`로 바뀜

![image](https://github.com/user-attachments/assets/25d75107-a3c0-4903-9103-e8489fa5c188)

---

## Auto 변경방법

Switch와 동일하나,
루프 만들기를 `한 번 실행` 또는 `루프`로 바꿔야함

---

## Skript API

| 구문                                |
|-------------------------------------|
|play animation of player to "애니메이션이름"|
|stop animation of player to "애니메이션이름"|

