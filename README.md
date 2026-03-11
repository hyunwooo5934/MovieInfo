# Introduce

Jetpack Compose & MVI 디자인 패턴 학습 프로젝트. 

## Project Goal
- Jetpack Compose 기능 심화 학습
- MVI + Compose 실무 적용 능력 향상
- SideEffect 학습
- 상태 기반 UI 설계
- 소셜 로그인

## Architecture
- Compose + MVI + Clean Architecture
  
![project_architecture](https://github.com/user-attachments/assets/47994f5f-5808-487b-aa51-66f4435513c5)

본 프로젝트는 확장 가능하고 예측 가능한 Android 애플리케이션 구조를 만들기 위해  
**MVI 아키텍처와 Clean Architecture**를 함께 적용했습니다.

Jetpack Compose는 상태(State) 기반으로 UI가 동작하는 프레임워크이기 때문에  
UI 상태를 **단일 상태 관리(Single Source of Truth)**와 **단방향 데이터 흐름(Unidirectional Data Flow)**으로 관리할 수 있는 MVI 패턴을 선택했습니다.

또한 Clean Architecture를 적용하여 프로젝트를 **Presentation, Domain, Data 계층**으로 분리하였으며,  
이를 통해 비즈니스 로직이 UI나 데이터 소스에 의존하지 않도록 구조를 설계했습니다.

이 구조를 통해 다음과 같은 이점을 얻을 수 있습니다.

- 예측 가능한 UI 상태 관리
- 관심사의 분리(Separation of Concerns)
- 테스트 용이성 및 유지보수성 향상


## Tech Stack
- Language : Kotlin
- Architecture : MVI, Clean Architecture
- UI : Jetpack Compose
- DI : Hilt
- Skils : Retrofit2, Google Login (추가예정.)


## 참조 링크
- 아키텍처 : https://jamshidbekboynazarov.medium.com/mvi-clean-architecture-unlocking-scalable-android-with-the-modern-trinity-ca23c78de9fa
