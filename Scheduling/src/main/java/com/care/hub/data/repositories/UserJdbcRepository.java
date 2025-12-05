package com.care.hub.data.repositories;

import com.care.hub.data.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> findByUsername(String username) {
        Optional<User> userOpt = jdbcClient.sql("""
                        SELECT id, username, password
                        FROM users
                        WHERE username = :username
                        """)
                .param("username", username)
                .query((rs, rowNum) -> {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    return u;
                })
                .optional();

        userOpt.ifPresent(u -> u.setRoles(findRolesByUserId(u.getId())));
        return userOpt;
    }

    public List<String> findRolesByUserId(Long userId) {
        return jdbcClient.sql("""
                        SELECT role
                        FROM user_roles
                        WHERE user_id = :userId
                        """)
                .param("userId", userId)
                .query(String.class)
                .list();
    }

    public Optional<Long> findUserIdByUsername(String username) {
        return jdbcClient.sql("""
                        SELECT id
                        FROM users
                        WHERE username = :username
                        """)
                .param("username", username)
                .query(Long.class)
                .optional();
    }

    public Optional<Long> findPatientIdByUserId(Long userId) {
        return jdbcClient.sql("""
                        SELECT id
                        FROM patients
                        WHERE user_id = :userId
                        """)
                .param("userId", userId)
                .query(Long.class)
                .optional();
    }

    public Optional<Long> findSchedulePatientIdByScheduleId(Long scheduleId) {
        return jdbcClient.sql("""
                        SELECT patient_id
                        FROM schedules
                        WHERE id = :id
                        """)
                .param("id", scheduleId)
                .query(Long.class)
                .optional();
    }
}
