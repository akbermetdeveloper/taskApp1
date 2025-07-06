package com.example.taskApp.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();


        logger.info("=== ВХОДЯЩИЙ ЗАПРОС ===");
        logger.info("Метод: {}", httpRequest.getMethod());
        logger.info("URL: {}", httpRequest.getRequestURL());
        logger.info("IP адрес: {}", httpRequest.getRemoteAddr());
        logger.info("User-Agent: {}", httpRequest.getHeader("User-Agent"));
        logger.info("Content-Type: {}", httpRequest.getContentType());


        chain.doFilter(request, response);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;


        logger.info("=== ИСХОДЯЩИЙ ОТВЕТ ===");
        logger.info("Статус код: {}", httpResponse.getStatus());
        logger.info("Время выполнения: {} мс", executionTime);
        logger.info("Content-Type ответа: {}", httpResponse.getContentType());
        logger.info("========================");
    }
}
