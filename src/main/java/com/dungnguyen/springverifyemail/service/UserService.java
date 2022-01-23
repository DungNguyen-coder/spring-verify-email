package com.dungnguyen.springverifyemail.service;


import com.dungnguyen.springverifyemail.entity.User;
import com.dungnguyen.springverifyemail.repo.UserRepo;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private JavaMailSender mailSender;

    public User register(User user, String siteURL){
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        repo.save(user);

        sendVerificationEmail(user, siteURL);
        return user;
    }

    public User verify(String verificationCode){
        Optional<User> userOp = Optional.ofNullable(repo.getUserByVerificationCode(verificationCode));

        return userOp.map(u -> {
            u.setEnabled(true);
            return u;
        }).orElse(null);

    }

    private void sendVerificationEmail(User user, String siteURL){
        String toAddress = user.getEmail();
        String fromAddress = "vandung1521@gmail.com";
        String senderName = "DUNG NGUYEN";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Dung Nguyen.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        try {
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}
