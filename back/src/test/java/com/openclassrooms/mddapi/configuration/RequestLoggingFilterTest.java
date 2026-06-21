package com.openclassrooms.mddapi.configuration;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.*;

class RequestLoggingFilterTest {

    @Test
    void doFilterInternal_ShouldExecuteSuccessfully() throws Exception {

        RequestLoggingFilter filter =
                new RequestLoggingFilter();

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setMethod("POST");
        request.setRequestURI("/api/test");
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", "Bearer token");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        response.setCharacterEncoding("UTF-8");

        FilterChain filterChain =
                mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1))
                .doFilter(any(), any());
    }

    @Test
    void doFilterInternal_ShouldHandleEmptyBodies() throws Exception {

        RequestLoggingFilter filter =
                new RequestLoggingFilter();

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setMethod("GET");
        request.setRequestURI("/api/empty");
        request.setCharacterEncoding("UTF-8");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        response.setCharacterEncoding("UTF-8");

        FilterChain filterChain =
                mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1))
                .doFilter(any(), any());
    }
}