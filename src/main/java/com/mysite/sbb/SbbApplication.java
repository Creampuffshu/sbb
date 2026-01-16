package com.mysite.sbb;

import com.mysite.sbb.repository.QuestionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SbbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbbApplication.class, args);
	}
    @Bean
    @Profile("local")
    public TestDataInit testDataInit(QuestionRepository questionRepository) {
        return new TestDataInit(questionRepository);
    }

}
