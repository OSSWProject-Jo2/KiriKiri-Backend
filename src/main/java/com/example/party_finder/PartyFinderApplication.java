package com.example.party_finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 프로젝트 전체에서 "날짜/시간 자동 기록(Auditing)" 기능을 활성화함
@SpringBootApplication
public class PartyFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartyFinderApplication.class, args);
    }

}
