package com.danko.danko_handmade.user;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.exception.EmailAlreadyExistsException;
import com.danko.danko_handmade.exception.UserNotFoundException;
import com.danko.danko_handmade.exception.UsernameAlreadyExistsException;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.Country;
import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.repository.UserRepository;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import com.danko.danko_handmade.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void givenNullUserFromDatabase_whenEditUserDetails_thenExceptionIsThrown() {

        UUID id = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder().build();
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.editUserDetails(id, dto));
    }

    @Test
    void givenExistingUserFromDatabase_whenEditUserDetails_thenUserProfileIsUpdatedAndUserIsSavedToDatabase() {
        UUID id = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .profilePicture("image.jpg")
                .phoneNumber("123456789")
                .streetNumber("10")
                .postalCode("12345")
                .city("London")
                .country(Country.BULGARIA)
                .recipientName("John Doe")
                .street("THis street")
                .build();

        User user = User.builder().build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.editUserDetails(id, dto);

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@doe.com", user.getEmail());
        assertEquals("image.jpg", user.getProfilePicture());
        assertEquals("123456789", user.getPhoneNumber());
        assertEquals("10", user.getStreetNumber());
        assertEquals("12345", user.getPostalCode());
        assertEquals("London", user.getCity());
        assertEquals("BULGARIA", user.getCountry().toString());
        assertEquals("John Doe", user.getRecipientName());
        assertEquals("THis street", user.getStreet());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithRoleAdmin_whenSwitchRole_thenUserGetsRoleUser() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .role(Role.ADMIN)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.switchRole(id);

        assertThat(user.getRole()).isEqualTo(Role.USER);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithRoleUser_whenSwitchRole_thenUserGetsRoleAdmin() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .role(Role.USER)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.switchRole(id);

        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserIsSubscribed_whenSwitchSubscription_thenUserBecomesNotSubscribed() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .subscribedToBulletin(true)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.switchSubscription(id);

        assertThat(user.isSubscribedToBulletin()).isFalse();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserIsNotSubscribed_whenSwitchSubscription_thenUserBecomesSubscribed() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .subscribedToBulletin(false)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.switchSubscription(id);

        assertThat(user.isSubscribedToBulletin()).isTrue();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserExistsInDatabase_whenLoadUserByUsername_thenUserAuthenticationIsReturned() {
        String username = "Pesho";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .role(Role.USER)
                .password("password")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);
        assertInstanceOf(AuthenticationMetadata.class, userDetails);
        AuthenticationMetadata result = (AuthenticationMetadata) userDetails;
        assertEquals(user.getId(), result.getUserId());
        assertEquals(username, result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
        assertThat(result.getAuthorities()).hasSize(1);
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void givenUserDoesNotExistInDatabase_whenLoadUserByUsername_thenExceptionIsThrown() {
        String username = "Pesho";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void givenUsernameExistsInDatabase_whenRegister_thenExceptionIsThrown() {
        RegisterRequest build = RegisterRequest.builder()
                .username("Pesho")
                .password("password")
                .email("pesho@doe.com")
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.register(build));
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any());
        verify(emailService, never()).sendEmail(any());
    }

    @Test
    void givenUsernameAndEmailDoNotExistInDatabase_whenRegister_thenUserIsRegistered() {
        RegisterRequest build = RegisterRequest.builder()
                .username("Pesho")
                .password("password")
                .email("pesho@doe.com")
                .build();
        User user = new User();

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        userService.register(build);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User createdUser = userCaptor.getValue();
        assertEquals("Pesho", createdUser.getUsername());
        assertEquals("pesho@doe.com", createdUser.getEmail());
        assertNotEquals("password", createdUser.getPassword());

        assertEquals("USER", createdUser.getRole().toString());
        assertThat(createdUser.getRegisteredOn()).isNotNull();
        assertThat(createdUser.isSubscribedToBulletin()).isTrue();

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(any());
        verify(passwordEncoder, times(1)).encode(build.getPassword());
    }

    @Test
    void givenEmailExistsInDatabase_whenRegister_thenExceptionIsThrown() {
        RegisterRequest build = RegisterRequest.builder()
                .username("Pesho")
                .password("password")
                .email("pesho@doe.com")
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(build));
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any());
        verify(emailService, never()).sendEmail(any());
    }

    @Test
    void givenDatabaseHasUsers_whenGetAllUsers_thenCorrectNumberIsReturned() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("Pesho")
                .password("password")
                .email("pesho@doe.com")
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("Gosho")
                .password("password1")
                .email("gosho@doe.com")
                .build();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void givenDatabaseIsEmpty_whenGetAllUsers_thenEmptyLIstIsReturned() {

        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAllUsers();
        assertEquals(0, users.size());
        assertThat(users).isNotNull();
    }

    @Test
    void givenSaveInitialUserIsCalled_thenUserIsSaved() {
        userService.saveInitialUser(new User());
        verify(userRepository, times(1)).save(any());
    }
}
