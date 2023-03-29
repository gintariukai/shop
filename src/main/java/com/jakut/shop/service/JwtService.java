package com.jakut.shop.service;

import com.jakut.shop.jwt.JwtProperties;
import com.jakut.shop.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String ROLES_CLAIM = "roles";
    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(User user) {

        Date now = new Date();

        return Jwts.builder()
                .setAudience("shop-client")
                .setIssuer("shop")
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getValidTimeInMillis()))
                .claim(ROLES_CLAIM, user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
                )
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKeyAsBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public User parseToken(String jwt) {

        Claims body = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKeyAsBytes()))
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return User.builder()
                .email(body.getSubject())
                .roles(((List<String>) body.get(ROLES_CLAIM)).stream()
                        .map(Role::new)
                        .collect(Collectors.toSet())) // ["ADMIN", "USER", ...] -> [new Role(...), new Role(...)]
                .build();
    }
}

