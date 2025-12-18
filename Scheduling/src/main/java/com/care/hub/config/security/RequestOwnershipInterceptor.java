package com.care.hub.config.security;

import com.care.hub.data.repositories.UserJdbcRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class RequestOwnershipInterceptor implements HandlerInterceptor {

    @Autowired
    private UserJdbcRepository userJdbcRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var path = request.getRequestURI();
        var method = request.getMethod();

        if (!"GET".equalsIgnoreCase(method)) {
            return true;
        }
        if (path == null || !path.startsWith("/schedules")) {
            return true;
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return true;
        }

        boolean isPatient = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_PATIENT"));

        if (!isPatient) {
            return true;
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
        if (path.equals("/schedules")) {
            var qPatientId = request.getParameter("patientId");
            if (qPatientId == null || !qPatientId.equals(myPatientIdStr)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Patients can only view their own appointments.");
                return false;
            }
            return true;
        }

        var parts = path.split("/");
        if (parts.length >= 3) {
            var last = parts[2];
            try {
                var scheduleId = Long.parseLong(last);
                var schedulePatientIdOpt = this.userJdbcRepository.findSchedulePatientIdByScheduleId(scheduleId);
                if (schedulePatientIdOpt.isEmpty() || !myPatientIdOpt.get().equals(schedulePatientIdOpt.get())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Patients can only view their own appointments.");
                    return false;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid resource.");
                return false;
            }
        }

        return true;
    }
}
