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
                INSERT INTO nurses (name, coren, nr_seq_user)
                VALUES (:name, :coren, :nr_seq_user)
                """)
                .param("name", nurse.getName())
                .param("coren", nurse.getCoren())
                .param("nr_seq_user", nurse.getUserId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;

        var id = keys.get("nr_seq_nurse");
        if (id != null) {
            nurse.setId(((Number)id).longValue());
        }

        return nurse;
    }

    public Optional<Nurse> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT nr_seq_nurse, name, coren
                        FROM nurses
                        WHERE nr_seq_nurse = :nr_seq_nurse
                        """)
                .param("nr_seq_nurse", id)
                .query(new NurseRowMapper())
                .optional();
    }

    public List<Nurse> findAll(int page, int perPage) {
        int offset = page * perPage;
        return jdbcClient.sql("""
                        SELECT nr_seq_nurse, name, coren
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
                            coren = :coren
                        WHERE nr_seq_nurse = :nr_seq_nurse
                        """)
                .param("name", nurse.getName())
                .param("coren", nurse.getCoren())
                .param("nr_seq_nurse", nurse.getId())
                .update();
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM nurses WHERE nr_seq_nurse = :nr_seq_nurse")
                .param("nr_seq_nurse", id)
                .update();
    }
}
