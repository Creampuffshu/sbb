package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Question;
import groovy.util.logging.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class QuestionRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(QuestionRepositoryTest.class);
    @Autowired private QuestionRepository questionRepository;


    @Test
    void findPage() {
        for(int i=0;i<100;i++){
            Question question = new Question();
            question.setCreateDate(LocalDateTime.now());
            question.setContent("content"+i);
            question.setSubject("subject"+i);
            questionRepository.save(question);
        }

        List<Question> page = questionRepository.findPage(95, 10);
        // 95 96 97 98 99
        assertThat(page.size()).isEqualTo(5);

        List<Question> page2 = questionRepository.findPage(100, 10);
        assertThat(page2.size()).isEqualTo(0);


    }

    @Test
    void count() {
    }
}