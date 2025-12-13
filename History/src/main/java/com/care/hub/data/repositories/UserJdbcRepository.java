package com.care.hub.data.repositories;

import com.care.hub.data.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public Optional<User> findByUsername(String username) {
        Optional<User> userOpt = jdbcClient.sql("""
                        SELECT nr_seq_user AS id, username, password
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
                        SELECT r.name
                        FROM user_roles ur
                        JOIN roles r ON r.nr_seq_role = ur.nr_seq_role
                        WHERE ur.nr_seq_user = :userId
                        """)
                .param("userId", userId)
                .query(String.class)
                .list();
    }

    public Optional<Long> findUserIdByUsername(String username) {
        return jdbcClient.sql("""
                        SELECT nr_seq_user
                        FROM users
                        WHERE username = :username
                        """)
                .param("username", username)
                .query(Long.class)
                .optional();
    }

    public Optional<Long> findPatientIdByUserId(Long userId) {
        return jdbcClient.sql("""
                        SELECT nr_seq_patient
                        FROM patients
                        WHERE nr_seq_user = :userId
                        """)
                .param("userId", userId)
                .query(Long.class)
                .optional();
    }

    public Optional<Long> findSchedulePatientIdByScheduleId(Long scheduleId) {
        return jdbcClient.sql("""
                        SELECT nr_seq_patient
                        FROM schedules
                        WHERE nr_seq_schedule = :id
                        """)
                .param("id", scheduleId)
                .query(Long.class)
                .optional();
    }

    public Optional<Long> findHistoryPatientIdByHistoryId(Long historyId) {
        return jdbcClient.sql("""
                        SELECT nr_seq_patient
                        FROM history_records
                        WHERE nr_seq_history_record = :id
                        """)
                .param("id", historyId)
                .query(Long.class)
                .optional();
    }
}
