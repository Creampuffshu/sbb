package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionRepository {

    private final JdbcTemplate template;

    public QuestionRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public Question save(Question question) {
        String sql = "insert into question(subject, content, create_date) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            //자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, question.getSubject());
            ps.setString(2, question.getContent());
            ps.setObject(3, question.getCreateDate());
            return ps;
        }, keyHolder);

        Integer key =  keyHolder.getKey().intValue();
        question.setId(key);
        return question;
    }

    public Optional<Question> findById(Integer id) {
        String sql = "select id,subject,content,create_date from question where id = ?";
        try{
            Question question = template.queryForObject(sql, questionRowMapper(), id);
            return Optional.of(question);
        }catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Question> findAll() {
        String sql = "select id,subject,content,create_date from question";
        return template.query(sql, questionRowMapper());
    }

    public Optional<Question> findBySubject(String subject) {
        String sql = "select * from question where subject = ?";
        try{
            Question question = template.queryForObject(sql, questionRowMapper(), subject);
            return Optional.of(question);
        }catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Question> findBySubjectLike(String subject) {
        String sql = "select * from question where subject like ?";
        return template.query(sql, questionRowMapper(), subject);
    }


    private RowMapper<Question> questionRowMapper() {
        return ((rs,rowNum)->{
            Question question = new Question();
            question.setId(rs.getInt("id"));
            question.setSubject(rs.getString("subject"));
            question.setContent(rs.getString("content"));
            question.setCreateDate(rs.getObject("create_date", LocalDateTime.class));
            return question;
        });
    }
}
