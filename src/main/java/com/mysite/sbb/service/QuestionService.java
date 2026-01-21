package com.mysite.sbb.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.PageResponse;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import com.mysite.sbb.repository.QuestionRepository;
import com.mysite.sbb.repository.UserRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user){
        log.info("create question");
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        questionRepository.save(q);
    }

    public PageResponse<Question> getPage(int page,String kw){
        Long total = questionRepository.count();
        int offset = page * 10;
        List<Question> questionList = questionRepository.findPage(offset, 10,kw);
        return new PageResponse<Question>(questionList, total, page, 10);

    }

    public void modify(Question question, String subject, String content) {
        questionRepository.update(question.getId(),subject,content,LocalDateTime.now());
    }

    public void delete(Question question){
        questionRepository.delete(question.getId());
    }

    public void vote(Question question,SiteUser siteUser){

        questionRepository.vote(question, siteUser);
    }

}
