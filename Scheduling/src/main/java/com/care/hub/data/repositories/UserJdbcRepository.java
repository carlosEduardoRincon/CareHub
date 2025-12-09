package com.care.hub.data.repositories;

import com.care.hub.data.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        var keys = keyHolder.getKeys();
        Objects.requireNonNull(keys, "Falha ao obter chaves geradas na inserção de users");
        var rawId = keys.get("nr_seq_user");
        if (rawId == null && keys.size() == 1) {
            rawId = keys.values().iterator().next();
        }
        if (!(rawId instanceof Number)) {
            throw new IllegalStateException("Chave gerada nr_seq_user inválida: " + rawId);
        }
        var userId = ((Number) rawId).longValue();
        user.setId(userId);

        if (user.getRoles() != null) {
            for (var roleName : user.getRoles()) {
                var roleId = jdbcClient.sql("""
                                INSERT INTO roles (name)
                                VALUES (:name)
                                ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name
                                RETURNING nr_seq_role
                                """)
                        .param("name", roleName)
                        .query(Long.class)
                        .single();

                jdbcClient.sql("""
                                INSERT INTO user_roles (nr_seq_user, nr_seq_role)
                                VALUES (:userId, :roleId)
                                ON CONFLICT (nr_seq_user, nr_seq_role) DO NOTHING
                                """)
                        .param("userId", userId)
                        .param("roleId", roleId)
                        .update();
            }
        }

        return user;
    }

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
}
