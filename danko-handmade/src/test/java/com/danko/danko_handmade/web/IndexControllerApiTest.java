package com.danko.danko_handmade.web;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.controller.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.View;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private View view;

    @Test
    void getRequestToIndexEndpoint_shouldReturnIndexView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }
}
