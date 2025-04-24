package com.project.board.exception.follow;

import com.project.board.exception.ClientErrorException;
import com.project.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowNotFoundException extends ClientErrorException {

    public FollowNotFoundException() {
        super(HttpStatus.NOT_FOUND, "follow not found");
    }

    public FollowNotFoundException(UserEntity follower, UserEntity following) {
        super(HttpStatus.NOT_FOUND, "follow with follower " + follower.getUsername() + " and following "
                + following.getUsername() + " not found");
    }
}
