package com.mysite.sbb.service;

import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import com.mysite.sbb.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Question question, String content, SiteUser author){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestionId(question.getId());
        answer.setAuthor(author);
        answerRepository.save(answer);
    }

    public List<Answer> getAnswerByQuestion(Integer questionId){
        return answerRepository.findByQuestionId(questionId);
    }
}
