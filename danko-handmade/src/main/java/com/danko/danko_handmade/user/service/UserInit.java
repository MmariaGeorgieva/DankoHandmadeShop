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

        User initialUserAdmin = User.builder()
                .username("DankoHandmade")
                .password(passwordEncoder.encode("123123"))
                .email("danko.pottery@gmail.com")
                .role(Role.ADMIN)
                .profilePicture("https://c8.alamy.com/comp/2J3B2T7/3d-illustration-of-smiling-businessman-close-up-portrait-cute-cartoon-man-avatar-character-face-isolated-on-white-background-2J3B2T7.jpg")
                .registeredOn(LocalDateTime.now())
                .subscribedToBulletin(true)
                .build();

        userService.saveInitialUser(initialUserAdmin);

        User initialUserUser = User.builder()
                .username("Mimozi")
                .password(passwordEncoder.encode("123123"))
                .email("maria.mariageorgieva@gmail.com")
                .role(Role.USER)
                .profilePicture("https://t4.ftcdn.net/jpg/09/61/69/75/240_F_961697523_EFd1m8P4tdcwB0TYvlQAagqKR1xHSuwk.jpg")
                .registeredOn(LocalDateTime.now())
                .subscribedToBulletin(true)
                .build();

        userService.saveInitialUser(initialUserUser);
    }
}
