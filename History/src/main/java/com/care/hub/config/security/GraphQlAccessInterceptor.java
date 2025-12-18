package com.care.hub.config.security;

import com.care.hub.data.repositories.UserJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.regex.Pattern;

@Component
public class GraphQlAccessInterceptor implements WebGraphQlInterceptor {

    private static final Pattern MUTATION_PATTERN = Pattern.compile("^\\s*mutation\\b", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    @Autowired
    private UserJdbcRepository userJdbcRepository;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return chain.next(request);
        }

        var authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        boolean isDoctor = authorities.contains("ROLE_DOCTOR");
        boolean isNurse = authorities.contains("ROLE_NURSE");
        boolean isPatient = authorities.contains("ROLE_PATIENT");

        String document = request.getDocument() == null ? "" : request.getDocument();
        boolean isMutation = MUTATION_PATTERN.matcher(document).find();

        if (isDoctor) {
            return chain.next(request);
        }

        if (isNurse) {
            if (isMutation) {
                return Mono.error(new AccessDeniedException("Nurses can only view the history."));
            }
            return chain.next(request);
        }

        if (isPatient) {
            if (isMutation) {
                return Mono.error(new AccessDeniedException("Patients cannot edit their medical history."));
            }

            var userIdOpt = this.userJdbcRepository.findUserIdByUsername(auth.getName());
            if (userIdOpt.isEmpty()) {
                return Mono.error(new AccessDeniedException("Access denied."));
            }

            var myPatientIdOpt = this.userJdbcRepository.findPatientIdByUserId(userIdOpt.get());
            if (myPatientIdOpt.isEmpty()) {
                return Mono.error(new AccessDeniedException("Access denied."));
            }

            Long myPatientId = myPatientIdOpt.get();
            Map<String, Object> vars = request.getVariables();

            Long patientIdFromVars = extractPatientIdFromVariables(vars);
            if (patientIdFromVars != null) {
                if (!myPatientId.equals(patientIdFromVars)) {
                    return Mono.error(new AccessDeniedException("Patients can only view their own medical history."));
                }
                return chain.next(request);
            }

            Long historyId = extractHistoryIdFromVariables(vars);
            if (historyId != null) {
                var historyPatientIdOpt = this.userJdbcRepository.findHistoryPatientIdByHistoryId(historyId);
                if (historyPatientIdOpt.isEmpty() || !myPatientId.equals(historyPatientIdOpt.get())) {
                    return Mono.error(new AccessDeniedException("Patients can only view their own medical history."));
                }
                return chain.next(request);
            }

            return Mono.error(new AccessDeniedException("Patients can only view their own medical history."));
        }

        return Mono.error(new AccessDeniedException("Access denied."));
    }

    private Long extractPatientIdFromVariables(Map<String, Object> vars) {
        if (vars == null || vars.isEmpty()) return null;

        Long direct = toLong(vars.get("patientId"));
        if (direct != null) return direct;

        Object filter = vars.get("filter");
        if (filter instanceof Map<?, ?> filterMap) {
            Long nested = toLong(((Map<?, ?>) filterMap).get("patientId"));
            if (nested != null) return nested;
        }

        Object input = vars.get("input");
        if (input instanceof Map<?, ?> inputMap) {
            Long nested = toLong(((Map<?, ?>) inputMap).get("patientId"));
            if (nested != null) return nested;

            Object nestedFilter = ((Map<?, ?>) inputMap).get("filter");
            if (nestedFilter instanceof Map<?, ?> nf) {
                Long nfPid = toLong(((Map<?, ?>) nf).get("patientId"));
                if (nfPid != null) return nfPid;
            }
        }

        return null;
    }

    private Long extractHistoryIdFromVariables(Map<String, Object> vars) {
        if (vars == null || vars.isEmpty()) return null;

        Long byId = toLong(vars.get("id"));
        if (byId != null) return byId;

        Long byHistoryId = toLong(vars.get("historyId"));
        if (byHistoryId != null) return byHistoryId;

        Object input = vars.get("input");
        if (input instanceof Map<?, ?> inputMap) {
            Long nestedId = toLong(((Map<?, ?>) inputMap).get("id"));
            if (nestedId != null) return nestedId;

            Long nestedHistoryId = toLong(((Map<?, ?>) inputMap).get("historyId"));
            if (nestedHistoryId != null) return nestedHistoryId;
        }

        return null;
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) {
            try {
                if (s.isBlank()) return null;
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}
