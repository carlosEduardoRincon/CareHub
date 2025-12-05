package com.care.hub.data.repositories;

import com.care.hub.data.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO users (username, password)
                VALUES (:username, :password)
                """)
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .update(keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            user.setId(generatedId.longValue());
        }

        if (user.getRoles() != null) {
            for (String role : user.getRoles()) {
                jdbcClient.sql("""
                                INSERT INTO user_roles (user_id, role)
                                VALUES (:userId, :role)
                                """)
                        .param("userId", user.getId())
                        .param("role", role)
                        .update();
            }
        }

        return user;
    }
}
