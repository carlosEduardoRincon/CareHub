package com.care.hub.data.repository;

import com.care.hub.data.entities.User;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbc;

//    public void save(User user) {
//        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
//        jdbc.update(sql, user.getUsername(), user.getPassword());
//    }
//
//    public User findByUsername(String username) {
//        String sql = "SELECT * FROM users WHERE username = ?";
//        return jdbc.query(sql, (rs, rowNum) -> {
//            User u = new User();
//            u.setId(rs.getLong("id"));
//            u.setUsername(rs.getString("username"));
//            u.setPassword(rs.getString("password"));
//            return u;
//        }, username);
//    }
}