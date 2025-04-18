package com.project.board.exception.user;

import com.project.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends ClientErrorException {

    public PasswordNotMatchException() {
        super(HttpStatus.BAD_REQUEST, "Password does not match.");
    }

}
