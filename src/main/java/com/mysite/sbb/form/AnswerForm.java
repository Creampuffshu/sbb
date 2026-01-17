package com.mysite.sbb.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnswerForm {
    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
