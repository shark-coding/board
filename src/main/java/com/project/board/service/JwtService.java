package com.project.board.service;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    // username전달받아 jwt토큰으로 발행, 해당 코튼을 검증할 때 토큰으로부터 username추출
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String getUsername(String accessToken) {
        return getSubject(accessToken);
    }

    private String generateToken(String subject) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + (1000 * 60 * 60 * 3));  // 만료시점

        return Jwts.builder().subject(subject).signWith(key).issuedAt(now).expiration(exp).compact();
    }

    private String getSubject(String token) {

        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        } catch (Exception e) {
            logger.error("JwtException", e);
            throw e;
        }

    }
}
