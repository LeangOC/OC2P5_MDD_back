package com.openclassrooms.mddapi.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);

        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        String requestBody = new String(
                wrappedRequest.getContentAsByteArray(),
                wrappedRequest.getCharacterEncoding()
        );

        String responseBody = new String(
                wrappedResponse.getContentAsByteArray(),
                wrappedResponse.getCharacterEncoding()
        );

        String headers = getHeaders(request);


        logger.info(
                """
                
                ================= HTTP REQUEST =================
                        - Method        : {}
                        - URI           : {}
                        - Headers       : {}
                        - Request Body  : {}
                        - Response Body : {}
                =================================================
                """,
                request.getMethod(),
                request.getRequestURI(),
                headers,
                requestBody.isBlank() ? "<empty>" : requestBody,
                responseBody.isBlank() ? "<empty>" : responseBody
        );
        wrappedResponse.copyBodyToResponse(); // sans cette ligne la connexion est impossible
    }
    private String getHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            sb.append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("\n");
        });

        return sb.toString();
    }
}
