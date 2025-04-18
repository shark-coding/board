package com.project.board.exception.user;

import com.project.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

    public UserNotAllowedException() {
        super(HttpStatus.FORBIDDEN, "User not allowed");
    }


}
