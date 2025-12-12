package com.care.hub.resources;

import com.care.hub.dtos.GraphQLHistoryRecordDTO;
import com.care.hub.data.entities.HistoryRecord;
import com.care.hub.data.repositories.HistoryRecordRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HistoryGraphQLController {

    private final HistoryRecordRepository repository;

    public HistoryGraphQLController(HistoryRecordRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public List<GraphQLHistoryRecordDTO> historyByPatient(@Argument String patientId, @Argument Boolean onlyFuture) {
        boolean future = Boolean.TRUE.equals(onlyFuture);
        List<HistoryRecord> records = future
                ? repository.findByPatientIdAndEventTimeAfterOrderByEventTimeAsc(patientId, Instant.now())
                : repository.findByPatientIdOrderByEventTimeAsc(patientId);

        return records.stream()
                .map(this::toGraphQL)
                .collect(Collectors.toList());
    }

    private GraphQLHistoryRecordDTO toGraphQL(HistoryRecord r) {
        String id = r.getId() == null ? null : String.valueOf(r.getId());
        String eventTime = r.getEventTime() == null ? null : r.getEventTime().toString();
        String createdAt = r.getCreatedAt() == null ? null : r.getCreatedAt().toString();
        return new GraphQLHistoryRecordDTO(
                id,
                r.getPatientId(),
                r.getAppointmentId(),
                r.getEventType(),
                eventTime,
                r.getPayload(),
                createdAt
        );
    }
}
