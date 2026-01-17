package com.mysite.sbb.domain;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUser {

    private Integer id;
    private String username;
    private String password;
    private String email;
}
