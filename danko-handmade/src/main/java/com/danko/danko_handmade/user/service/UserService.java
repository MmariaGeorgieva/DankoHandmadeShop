package com.danko.danko_handmade.user.service;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.exception.EmailAlreadyExistsException;
import com.danko.danko_handmade.exception.UserNotFoundException;
import com.danko.danko_handmade.exception.UsernameAlreadyExistsException;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.repository.UserRepository;
import com.danko.danko_handmade.web.dto.EmailRequest;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import com.danko.danko_handmade.web.dto.UserEditRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        Optional<User> optionalUser1 = userRepository.findByEmail(registerRequest.getEmail());
        if (optionalUser1.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .email(registerRequest.getEmail())
                .registeredOn(LocalDateTime.now())
                .subscribedToBulletin(true)
                .build();
        userRepository.save(user);
        log.info("User {%s} with id {%s} has been registered.".formatted(user.getUsername(), user.getId()));

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setUser(user);
        emailRequest.setSubject("User Registration");
        String body = String.format(
                "Dear %s,%n" +
                        "Welcome to DankoHandmade!%n" +
                        "We’re thrilled to have you join our community. You now have access to browse our unique collection of handmade ceramic pieces. Explore our products and find something special for yourself or as a thoughtful gift for a loved one.%n" +
                        "We strive to offer you only the best, with quality creations inspired by the craftsmanship of our artisans.%n" +
                        "If you have any questions or need assistance, feel free to reach out to us. Enjoy shopping!%n" +
                        "Best regards,%n" +
                        "The DankoHandmade Team",
                user.getUsername()
        );
        emailRequest.setBody(body);
        emailService.sendEmail(emailRequest);
    }

    public void saveInitialUser(User initialUser) {
        userRepository.save(initialUser);
        log.info("User {%s} with id {%s} has been registered.".formatted(initialUser.getUsername(), initialUser.getId()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found"));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole());
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void switchRole(UUID id) {
        User user = getById(id);
        if (user.getRole() == Role.USER) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        userRepository.save(user);
    }

    public void editUserDetails(UUID id, @Valid UserEditRequest userEditRequest) {
        User user = getById(id);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setEmail(userEditRequest.getEmail());
        user.setProfilePicture(userEditRequest.getProfilePicture());
        user.setRecipientName(userEditRequest.getRecipientName());
        user.setCountry(userEditRequest.getCountry());
        user.setCity(userEditRequest.getCity());
        user.setPostalCode(userEditRequest.getPostalCode());
        user.setStreet(userEditRequest.getStreet());
        user.setStreetNumber(userEditRequest.getStreetNumber());
        user.setPhoneNumber(userEditRequest.getPhoneNumber());

        userRepository.save(user);
    }

    public void switchSubscription(UUID id) {
        User user = getById(id);
        user.setSubscribedToBulletin(!user.isSubscribedToBulletin());
        userRepository.save(user);
    }
}
