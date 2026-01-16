package com.mysite.sbb.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Answer {
    private Integer id;
    private String content;
    private LocalDateTime createDate;
    private Integer questionId;
}
