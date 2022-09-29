package com.example.projform.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final SendMailService sendMailService;

    @PostMapping("/register")
    void register(@RequestBody UserRequest userRequest){

        userService.register(userRequest);
    }

    @GetMapping(value = "/qr/{id}", produces = MediaType.IMAGE_PNG_VALUE)
     BufferedImage getQrCode(@PathVariable("id") Long id ) throws Exception {
        return sendMailService.getQrCodeForUser(id);
    }
}
