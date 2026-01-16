package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save(){
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);

        Answer answer = new Answer();
        answer.setQuestionId(q1.getId());
        answer.setCreateDate(LocalDateTime.now());
        answer.setContent("저도 모르겠습니다.");
        answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findById(answer.getId());
        assertThat(findAnswer.get().getId()).isEqualTo(answer.getId());
    }

    @Test
    void findByQuestionId(){
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);

        Answer answer = new Answer();
        answer.setQuestionId(q1.getId());
        answer.setCreateDate(LocalDateTime.now());
        answer.setContent("저도 모르겠습니다.");
        answerRepository.save(answer);

        Answer answer2 = new Answer();
        answer2.setQuestionId(q1.getId());
        answer2.setCreateDate(LocalDateTime.now());
        answer2.setContent("저도 모르겠습니다.");
        answerRepository.save(answer2);

        Answer answer3 = new Answer();
        answer3.setQuestionId(q1.getId());
        answer3.setCreateDate(LocalDateTime.now());
        answer3.setContent("저도 모르겠습니다.");
        answerRepository.save(answer3);



        List<Answer> answerList = answerRepository.findByQuestionId(q1.getId());
        assertThat(answerList.size()).isEqualTo(3);
    }
}