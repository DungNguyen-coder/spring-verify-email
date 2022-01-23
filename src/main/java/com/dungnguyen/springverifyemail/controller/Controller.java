package com.dungnguyen.springverifyemail.controller;


import com.dungnguyen.springverifyemail.entity.User;
import com.dungnguyen.springverifyemail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller {
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public User Register(@RequestBody User user, HttpServletRequest request){
        return service.register(user, getSiteURL(request));

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code){
        if (service.verify(code) != null){
            return "Verified Email. You can login with this Account";
        } else {
            return "Verify Fail !";
        }
    }
}
