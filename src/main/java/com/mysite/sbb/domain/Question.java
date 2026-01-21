package com.mysite.sbb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Getter
@Setter
public class Question {

    private Integer id;

    private String subject;

    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    private SiteUser author;

    private int answerCount;

    private int voteCount;
}
