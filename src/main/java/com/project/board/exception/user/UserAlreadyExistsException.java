package com.project.board.exception.user;

import com.project.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ClientErrorException {

    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "User already exists");
    }

    public UserAlreadyExistsException(String username) {
        super(HttpStatus.CONFLICT, "User with postID" + username + " already exists");
    }

}
