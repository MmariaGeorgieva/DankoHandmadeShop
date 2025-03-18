package com.danko.danko_handmade.user.service;

import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserInit implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInit(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if(!userService.getAllUsers().isEmpty()) {
            return;
        }

        User initialUser = User.builder()
                .username("DankoHandmade")
                .password(passwordEncoder.encode("123123"))
                .email("shop@danko.com")
                .role(Role.ADMIN)
                .registeredOn(LocalDateTime.now())
                .subscribedToBulletin(true)
                .build();

        userService.saveInitialUser(initialUser);
    }
}
