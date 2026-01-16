package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AnswerRepository {

    private final JdbcTemplate template;

    public AnswerRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Answer save(Answer answer) {
        String sql = "insert into answer(content,create_date,question_id) values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            //자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, answer.getContent());
            ps.setObject(2, answer.getCreateDate());
            ps.setObject(3, answer.getQuestionId());
            return ps;
        }, keyHolder);

        Integer key =  keyHolder.getKey().intValue();
        answer.setId(key);
        return answer;
    }

    public Optional<Answer> findById(Integer id) {
        String sql = "select * from answer where id = ?";
        try{
            Answer answer = template.queryForObject(sql, answerRowMapper(), id);
            return Optional.of(answer);
        }catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Answer> findByQuestionId(Integer questionId) {
        String sql = "select * from answer where question_id = ?";
        return template.query(sql, answerRowMapper(), questionId);
    }

    private RowMapper<Answer> answerRowMapper() {
        return ((rs,rowNum)->{
            Answer answer = new Answer();
            answer.setId(rs.getInt("id"));
            answer.setQuestionId(rs.getInt("question_id"));
            answer.setContent(rs.getString("content"));
            answer.setCreateDate(rs.getObject("create_date", LocalDateTime.class));
            return answer;
        });
    }
}
