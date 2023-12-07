

# blossom-framework

> 해당 프로젝트는 나만의 spring-framework 를 개발하기 위한 프로젝트이다. 

## core

> spring-core 에 해당한다.

### feature
업데이트 - 2023/12/05

#### Feature
- 설정 Class 기반 Bean 등록
- Bean 의 이름, 타입으로 조회 기능
- Dependency Injection
  - Setter 주입 기능

#### Details 
- 설정 Bean 클래스 등록 후 DI(refresh) 기능 
- Bean 의 정의와 생성(조립)을 분리
  - BeanDefinition, BeanRegistry 기능
    - BeanDefinition 은 Bean 의 Meta 정보 저장
    - BeanRegistry 은 BeanDefinition 을 기반으로 조립 후 생성된 Bean 저장소

#### 추가 예정
- 싱글톤 기능 확립
- bean 조립 시 타입 이름이 아닌, 필드명으로 조립  
- Parent Type 조회
- BeanRegistry 클래스 분리
- DI 전략 추가
  - 생성자 주입, 필드 주입
- 순환참조
- 팩토리 메서드의 파라미터 사용 시 bean 조회 기능 