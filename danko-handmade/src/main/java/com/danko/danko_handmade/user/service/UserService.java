package com.danko.danko_handmade.user.service;

import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.repository.UserRepository;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("Username %s is already in use.".formatted(registerRequest.getUsername()));
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .email(registerRequest.getEmail())
                .registeredOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("User {%s} with id {%s} has been registered.".formatted(user.getUsername(), user.getId()));
    }

    public void saveInitialUser(User initialUser) {
        userRepository.save(initialUser);
        log.info("User {%s} with id {%s} has been registered.".formatted(initialUser.getUsername(), initialUser.getId()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found"));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole());
    }
}
