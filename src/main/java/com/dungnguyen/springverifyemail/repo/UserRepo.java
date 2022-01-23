package com.dungnguyen.springverifyemail.repo;

import com.dungnguyen.springverifyemail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    User getUserByVerificationCode(String verificationCode);
}
