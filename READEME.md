### 데이터베이스 ui 다활용 가능한 곳(다운 필요)
https://tableplus.com/

https://sos0911.github.io/tableplus/tableplus/ 

### 참고 github
https://github.com/dev-jayce/crash

### yml

스프링은 기본적으로 application.yml 을 기본적으로 사용한다

이렇게 별도로 구성해서 사용한다
application.yml(공통적으로 적용되어야 할 설정들을 여기서 설정)
application-dev.yml(개발용도 = 테스트환경의 데이터베이스 설정)
application-prod.yml(실제 서비스용도 = 여기는 테스트환경 데이터베이스를 꼭 다른걸 써야한다 (중요) = 지금 이 프로젝트는 연습이여서 설정 같은걸 사용)
그리고 application.yml에서 어떤 yml을 사용할건지 설정을 꼭 해줘야 한다 

설정 방법은

1.edit configurations 에들어가서
2.gradle을 추가하고
3.run 부분에
4.bootRun --args='--spring.profiles.active=[yml 파일에서 dev 이부분을 작성해주면 된다]' 이거를 작성해주면 끝이다
ex)bootRun --args='--spring.profiles.active=dev'


다른 설정방법
spring:
    profiles:
        active:
            - dev

이거를 기본 yml 파일에 추가하면 된다 


또 다른 방법으로는 

edit configurations 에들어가서
application 부분에
vm options를 추가한다  
vm options 에다가 -Dspring.profiles.active=dev 이거를 작성해주면 끝이다
