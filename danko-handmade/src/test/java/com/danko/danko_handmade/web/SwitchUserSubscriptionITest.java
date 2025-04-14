package com.danko.danko_handmade.web;

import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.repository.UserRepository;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class SwitchUserSubscriptionITest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void unsubscribeFromNewsletter_HappyPath() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("danko")
                .email("danko@gmail.com")
                .password("123123")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("danko").get();
        UUID userId = user.getId();

        userService.switchSubscription(userId);
        User updatedUser = userRepository.findById(userId).get();

//        assertThat(String.valueOf(user.isSubscribedToBulletin()), true);
        assertFalse(updatedUser.isSubscribedToBulletin());

    }
}
