package com.erp.report_service.util;

import com.erp.report_service.exception.ReportException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public class RequestContext {

    public static String getUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }

    public static String getRoles(HttpServletRequest request) {
        return request.getHeader("X-User-Roles");
    }

    public static void requireAdmin(HttpServletRequest request) {
        String roles = getRoles(request);
        if (roles == null || !roles.contains("ADMIN")) {
            throw new ReportException(
                    "Access denied: ADMIN role required",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}