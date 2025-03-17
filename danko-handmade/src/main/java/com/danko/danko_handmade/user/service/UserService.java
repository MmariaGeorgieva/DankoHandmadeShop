package com.danko.danko_handmade.user.service;

import com.danko.danko_handmade.address.model.Address;
import com.danko.danko_handmade.exception.UsernameAlreadyExistsException;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.repository.UserRepository;
import com.danko.danko_handmade.web.dto.AddAddressRequest;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import com.danko.danko_handmade.web.dto.UserEditRequest;
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
            throw new UsernameAlreadyExistsException("Username %s is already in use.".formatted(registerRequest.getUsername()));
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

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
        user.setPhone(userEditRequest.getPhone());
        user.setProfilePicture(userEditRequest.getProfilePicture());

        userRepository.save(user);
    }


    public void addAddressToUserWithId(UUID userId, @Valid AddAddressRequest addAddressRequest) {
        User user = getById(userId);
        Address address = Address.builder()
                .id(UUID.randomUUID())
                .recipientName(addAddressRequest.getRecipientName())
                .city(addAddressRequest.getCity())
                .country(addAddressRequest.getCountry())
                .street(addAddressRequest.getStreet())
                .streetNumber(addAddressRequest.getStreetNumber())
                .phoneNumber(addAddressRequest.getPhoneNumber())
                .postalCode(addAddressRequest.getPostalCode())
                .build();

        user.getUserAddressList().add(address);
        userRepository.save(user);
    }
}
