package com.example.projform.user;

import lombok.AllArgsConstructor;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final SendMailService sendMailService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    void register(UserRequest userRequest){

        User user = new User(
                generateCode(),
                userRequest.getLastName(),
                userRequest.getFirstName(),
                userRequest.getEmail(),
                userRequest.getPhone()
        );
        sendMail(userRepository.save(user));
    }

    boolean sendMail(User user){
        try {
            sendMailService.sendMail(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    String generateCode(){
        return UUID.randomUUID().toString();
    }



}
