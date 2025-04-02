package com.danko.danko_handmade.user;

import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserInit;
import com.danko.danko_handmade.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserInitUTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserInit userInit;

    @Test
    void givenUsersExist_whenRun_thenNoUsersAreCreated() throws Exception {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .role(Role.USER)
                .password("password")
                .build();

        when(userService.getAllUsers()).thenReturn(List.of(user));

        userInit.run();
        verify(userService, never()).saveInitialUser(any());
    }

    @Test
    void givenUsersDoNotExist_whenRun_twoUsersAreCreated() throws Exception {

        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        userInit.run();
        verify(userService, times(2)).saveInitialUser(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(2)).saveInitialUser(userCaptor.capture());
        List<User> createdUsers = userCaptor.getAllValues();
        assertEquals(2, createdUsers.size());
        assertEquals("DankoHandmade", createdUsers.get(0).getUsername());
        assertEquals("Mimozi", createdUsers.get(1).getUsername());
    }
}
