package com.jakut.shop.jwt;

import com.jakut.shop.model.Role;
import com.jakut.shop.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    public static String ROLES_CLAIM = "roles";

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.token.prefix}")
    private String jwtTokenPrefix;

    @Value("${app.jwt.header.string}")
    private String jwtHeaderString;

    @Value("${app.jwt.expiration-in-ms}")
    private Long jwtExpirationInMs;

    public String createToken(User user) {

        Date now = new Date();

        return Jwts.builder()
                .setAudience("shop-client")
                .setIssuer("shop")
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationInMs))
                .claim(ROLES_CLAIM, user.getRole())
                .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public User parseToken(String jwt) {

        Claims body = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .parseClaimsJws(jwt)
                .getBody();

        String claimRole = (String) body.get(ROLES_CLAIM);

        User user = new User();
        user.setUsername(body.getSubject());
        user.setRole(Role.valueOf(claimRole));

        return user;
    }

}