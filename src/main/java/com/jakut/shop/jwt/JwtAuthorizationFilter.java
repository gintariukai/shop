package com.jakut.shop.jwt;

import com.jakut.shop.service.JwtService;
import jakarta.servlet.*;

import java.io.IOException;

public class JwtAuthorizationFilter implements Filter {
    public JwtAuthorizationFilter(JwtService jwtService) {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
