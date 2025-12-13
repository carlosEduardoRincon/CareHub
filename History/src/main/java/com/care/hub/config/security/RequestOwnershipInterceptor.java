package com.care.hub.config.security;

import com.care.hub.data.repositories.UserJdbcRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestOwnershipInterceptor implements HandlerInterceptor {

    @Autowired
    private UserJdbcRepository userJdbcRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var path = request.getRequestURI();
        var method = request.getMethod();

        if (path == null || !path.startsWith("/history")) {
            return true;
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return true;
        }

        var authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        boolean isDoctor = authorities.contains("ROLE_DOCTOR");
        boolean isNurse = authorities.contains("ROLE_NURSE");
        boolean isPatient = authorities.contains("ROLE_PATIENT");

        if (isDoctor) {
            return true;
        }

        if (isNurse) {
            if (!"GET".equalsIgnoreCase(method)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Nurses can only view the history.");
                return false;
            }
            return true;
        }

        if (isPatient) {
            if (!"GET".equalsIgnoreCase(method)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Patients cannot edit their medical history.");
                return false;
            }

            var userIdOpt = this.userJdbcRepository.findUserIdByUsername(auth.getName());
            if (userIdOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return false;
            }

            var myPatientIdOpt = this.userJdbcRepository.findPatientIdByUserId(userIdOpt.get());
            if (myPatientIdOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return false;
            }

            var myPatientIdStr = String.valueOf(myPatientIdOpt.get());
            if (path.equals("/history")) {
                var qPatientId = request.getParameter("patientId");
                if (qPatientId == null || !qPatientId.equals(myPatientIdStr)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Patients can only view their own medical history.");
                    return false;
                }
                return true;
            }

            var parts = path.split("/");
            if (parts.length >= 3) {
                var last = parts[2];
                try {
                    var historyId = Long.parseLong(last);
                    var historyPatientIdOpt = this.userJdbcRepository.findHistoryPatientIdByHistoryId(historyId);
                    if (historyPatientIdOpt.isEmpty() || !myPatientIdOpt.get().equals(historyPatientIdOpt.get())) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Patients can only view their own medical history.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid resource.");
                    return false;
                }
            }

            return true;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
        return false;
    }
}
