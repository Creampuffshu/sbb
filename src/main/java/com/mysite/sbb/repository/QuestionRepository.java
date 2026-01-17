package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
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
        String sql = "insert into question(subject, content, create_date, modify_date, author_id) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            //자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, question.getSubject());
            ps.setString(2, question.getContent());
            ps.setObject(3, question.getCreateDate());
            ps.setObject(4, question.getModifyDate());
            ps.setObject(5, question.getAuthor().getId());
            return ps;
        }, keyHolder);

        Integer key =  keyHolder.getKey().intValue();
        question.setId(key);
        return question;
    }

    public Optional<Question> findById(Integer id) {
        String sql = "select q.* ,(select count(*) from answer a where a.question_id = q.id) as answer_count, " +
                "u.id as u_id, u.username, u.password, u.email " +
                "from question q left join site_user u on q.author_id = u.id where q.id = ?";

        try{
            Question question = template.queryForObject(sql, questionWithAuthorRowMapper(), id);
            return Optional.of(question);
        }catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Question> findAll() {
        String sql = "select * from question";
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
            question.setModifyDate(rs.getObject("modify_date", LocalDateTime.class));

            return question;
        });
    }

    private RowMapper<Question> questionWithAuthorRowMapper() {
        return ((rs,rowNum)->{
            Question question = new Question();
            question.setId(rs.getInt("id"));
            question.setSubject(rs.getString("subject"));
            question.setContent(rs.getString("content"));
            question.setCreateDate(rs.getObject("create_date", LocalDateTime.class));
            question.setModifyDate(rs.getObject("modify_date", LocalDateTime.class));
            question.setAnswerCount(rs.getInt("answer_count"));


            SiteUser siteUser = new SiteUser();
            siteUser.setId(rs.getInt("u_id"));
            siteUser.setUsername(rs.getString("username"));
            siteUser.setPassword(rs.getString("password"));
            siteUser.setEmail(rs.getString("email"));

            question.setAuthor(siteUser);
            return question;
        });
    }

    public List<Question> findPage(int offset, int size) {
        String sql = "select q.* ,(select count(*) from answer a where a.question_id = q.id) as answer_count, " +
                "u.id as u_id, u.username, u.password, u.email " +
                "from question q left join site_user u on q.author_id = u.id order by q.create_date desc limit ? offset ?";

        return template.query(sql, questionWithAuthorRowMapper(), size, offset);
    }

    public Long count(){
        String sql = "select count(*) from question";
        return template.queryForObject(sql,Long.class);
    }

    public void update(Integer questionId, String subject, String content, LocalDateTime modifyDate) {
        String sql = "update question set subject = ?, content = ?, modify_date = ? where id = ?";
        template.update(sql, subject, content, modifyDate,questionId);
    }

    public void delete(Integer questionId) {
        String sql = "delete from question where id = ?";
        template.update(sql, questionId);
    }
}
