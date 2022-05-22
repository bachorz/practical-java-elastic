package com.course.practicaljavaelastic.api.server;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefaultRestApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void welcomeTest() {
        webTestClient.get().uri("/api/welcome").exchange().expectStatus().is2xxSuccessful().expectBody(String.class)
                        .value(IsEqualIgnoringCase.equalToIgnoringCase("Zajebioza witamy wszystkich"));
    }

    @Test
    void timeTest() {
        webTestClient.get().uri("/api/time").exchange().expectBody(String.class).value(v -> isCorrectTime(v));
    }

    private Object isCorrectTime(String v){
        LocalTime responseLocalTime = LocalTime.parse(v);
        LocalTime now = LocalTime.now();
        LocalTime nowMinus30Seconds = now.minusSeconds(30);

        assertTrue(responseLocalTime.isAfter(nowMinus30Seconds) && responseLocalTime.isBefore(now));

        return responseLocalTime;
    }

    @Test
    void headerByAnnotationTest() {
        String headerOne = "Spring Boot Test";
        String headerTwo = "Spring Boot Test on Practical Java Elastic";

        webTestClient.get().uri("/api/header-one").header("User-agent", headerOne).header("Practical-java-elastic", headerTwo)
                .exchange().expectBody(String.class).value(v -> {
                    assertTrue(v.contains(headerOne));
                    assertTrue(v.contains(headerTwo));
        });
    }

    @Test
    void headerByRequest() {
    }
}