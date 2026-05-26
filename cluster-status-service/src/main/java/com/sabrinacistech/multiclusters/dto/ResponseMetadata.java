package com.sabrinacistech.multiclusters.dto;

import com.sabrinacistech.multiclusters.util.LogSanitizer;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

public record ResponseMetadata(
    String appName,
    String projectName,
    String clientClusterActive,
    String cluster,
    String method,
    String path,
    String timestamp
) {

    public static ResponseMetadata from(
        HttpServletRequest request,
        String appName,
        String projectName,
        String clientClusterActive,
        String cluster,
        OffsetDateTime timestamp
    ) {
        return new ResponseMetadata(
            LogSanitizer.sanitizeForLog(appName),
            LogSanitizer.sanitizeForLog(projectName),
            LogSanitizer.sanitizeForLog(clientClusterActive),
            cluster,
            request.getMethod(),
            request.getRequestURI(),
            timestamp.toString()
        );
    }
}
