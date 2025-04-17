package com.project.board.model.user;

public record UserSignUpRequestBody(
        String username,
        String password
){}