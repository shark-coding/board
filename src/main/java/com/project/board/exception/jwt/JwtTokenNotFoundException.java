package com.project.board.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtTokenNotFoundException extends JwtException {
    public JwtTokenNotFoundException() {
        super("Jwt token not found");
    }
}
