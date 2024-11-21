package com.whatsapp.verficacion.ApiKeyFilter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter{
    private final ApiKeyProperties apiKeyProperties;

    public ApiKeyFilter(ApiKeyProperties apiKeyProperties) {
        this.apiKeyProperties = apiKeyProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
        //String apiKey = request.getHeader(apiKeyProperties.getHeader());
        //if (apiKey == null || !apiKey.equals(apiKeyProperties.getValue())){
        //    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //   response.getWriter().write("Unauthorized: Contact to IT.");
        //    return;
        //}

        filterChain.doFilter(request, response);    
    }
}
