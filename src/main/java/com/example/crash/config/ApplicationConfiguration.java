package com.example.crash.config;


import com.example.crash.model.sessionspeaker.SessionSpeaker;
import com.example.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.example.crash.model.user.UserSignUpRequestBody;
import com.example.crash.service.SessionSpeakerService;
import com.example.crash.service.UserService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class ApplicationConfiguration {

    private static final Faker faker = new Faker();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override //spring application을 구동시키는 동시에 run이 동작을 한다 이말은 스프링부트를 시작하면 알아서 더미데이터를 추가한다
            public void run(ApplicationArguments args) throws Exception {
                //TODO: 유저 및 세션스피커 생성
                createTestUsers();

                createTestSessionSpeakers(10);
            }
        };
    }

    //더미 데이터(유저)
    private void createTestUsers() {
        userService.signUp(new UserSignUpRequestBody("jayce", "1234", "Dev Jayce", "jayce@crash.com"));
        userService.signUp(new UserSignUpRequestBody("jay", "1234", "Dev Jay", "jay@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rose", "1234", "Dev Rose", "rose@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rosa", "1234", "Dev Rosa", "rosa@crash.com"));

        // 이렇게 faker를 통해 네임 등 을 생성할 수 있다
        //userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(), faker.internet().emailAddress()));
    }

    // 반복 호출
    private void createTestSessionSpeakers(int numberOfSpeakers) {
        IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker()).toList();
    }

    private SessionSpeaker createTestSessionSpeaker() {
        var name = faker.name().fullName();
        var company =faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
                new SessionSpeakerPostRequestBody(company,name,description));
    }


}
