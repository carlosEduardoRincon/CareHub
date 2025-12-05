package com.care.hub.data.repositories;

import com.care.hub.data.entities.Nurse;
import com.care.hub.data.rowmapper.NurseRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NurseJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public Nurse save(Nurse nurse) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO nurses (name, login, password, cpf, coren)
                VALUES (:name, :login, :password, :cpf, :coren)
                """)
                .param("name", nurse.getName())
                .param("login", nurse.getLogin())
                .param("password", nurse.getPassword())
                .param("cpf", nurse.getCpf())
                .param("coren", nurse.getCoren())
                .update(keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            nurse.setId(generatedId.longValue());
        }
        return nurse;
    }

    public Optional<Nurse> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT id, name, login, password, cpf, coren
                        FROM nurses
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(new NurseRowMapper())
                .optional();
    }

    public List<Nurse> findAll(int page, int perPage) {
        int offset = page * perPage;
        return jdbcClient.sql("""
                        SELECT id, name, login, password, cpf, coren
                        FROM nurses
                        LIMIT :limit OFFSET :offset
                        """)
                .param("limit", perPage)
                .param("offset", offset)
                .query(new NurseRowMapper())
                .list();
    }

    public int count() {
        return jdbcClient.sql("SELECT COUNT(*) FROM nurses")
                .query(Integer.class)
                .single();
    }

    public void update(Nurse nurse) {
        jdbcClient.sql("""
                        UPDATE nurses
                        SET name = :name,
                            cpf = :cpf,
                            coren = :coren
                        WHERE id = :id
                        """)
                .param("name", nurse.getName())
                .param("cpf", nurse.getCpf())
                .param("coren", nurse.getCoren())
                .param("id", nurse.getId())
                .update();
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM nurses WHERE id = :id")
                .param("id", id)
                .update();
    }
}
