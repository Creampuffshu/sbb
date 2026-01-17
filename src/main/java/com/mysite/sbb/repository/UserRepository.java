package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Question;
import com.mysite.sbb.domain.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate template;

    public UserRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public SiteUser save(SiteUser siteUser){
        String sql = "insert into site_user(username,password, email) values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            //자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, siteUser.getUsername());
            ps.setString(2, siteUser.getPassword());
            ps.setObject(3, siteUser.getEmail());
            return ps;
        }, keyHolder);

        Integer key =  keyHolder.getKey().intValue();
        siteUser.setId(key);
        return siteUser;
    }

    public Optional<SiteUser> findByUsername(String username){
        String sql = "select * from site_user where username = ?";
        try{
            SiteUser user = template.queryForObject(sql, userRowMapper(), username);
            return Optional.of(user);
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    private RowMapper<SiteUser> userRowMapper() {
        return ((rs,rowNum)->{
            SiteUser user = new SiteUser();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            return user;
        });
    }
}
