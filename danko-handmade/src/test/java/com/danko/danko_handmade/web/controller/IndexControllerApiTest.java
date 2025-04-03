package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.View;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
@AutoConfigureMockMvc(addFilters = false)
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
        MockHttpServletRequestBuilder request = get("/");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    void getRequestToLoginEndpointWithErrorParameter_shouldReturnLoginViewAndErrorMessageAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/login").param("error", "");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest", "errorMessage"));
    }

    @Test
    void getRequestToLoginEndpointWithoutErrorParameter_shouldReturnLoginViewWithoutErrorMessageAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"))
                .andExpect(model().attributeDoesNotExist("errorMessage"));
    }

    @Test
    void getRequestToRegisterEndpoint_shouldReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void postRequestToRegisterEndpoint_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "Mimozi")
                .formField("password", "123123")
                .formField("email", "test@test.com")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpoint_whenInvalidDataProvided_thenReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "")
                .formField("password", "")
                .formField("email", "test@test.com")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
        verify(userService, never()).register(any());
    }

//    @Test
//    void postRequestToRegisterEndpoint_whenUsernameAlreadyExists_thenRedirectToRegisterViewWithFlashParam() throws Exception {
//
//        doThrow(new UsernameAlreadyExistsException()).when(userService).register(any(RegisterRequest.class));
//
//        MockHttpServletRequestBuilder request = post("/register")
//                .formField("username", "")
//                .formField("password", "")
//                .formField("email", "test@test.com")
//                .with(csrf());
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(view().name("register"))
//                .andExpect(flash().attributeExists("usernameAlreadyExistsMessage"));
//        verify(userService, never()).register(any());
//    }

    @Test
    @WithMockUser(username = "test-user")
    void getRequestToAboutEndpointAsAuthenticatedUser_shouldReturnAboutViewWithUserAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/about");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void getRequestToAboutEndpointAsUnauthenticatedUser_shouldReturnAboutViewWithoutUserAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/about");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attributeDoesNotExist("user"));
    }

    @Test
    @WithMockUser(username = "test-user")
    void getRequestToShopPoliciesEndpointAsAuthenticatedUser_shouldReturnShopPoliciesViewWithUserAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/shop-policies");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("shop-policies"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void getRequestToShopPoliciesEndpointAsUnauthenticatedUser_shouldReturnShopPoliciesViewWithoutUserAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/shop-policies");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("shop-policies"))
                .andExpect(model().attributeDoesNotExist("user"));
    }

    @Test
    void getRequestToContactEndpoint_withoutSuccessParamAndUnauthenticatedUser_shouldReturnContactView() throws Exception {
        MockHttpServletRequestBuilder request = get("/contact");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactShopRequest"))
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attributeDoesNotExist("user"))
                .andExpect(model().attributeDoesNotExist("userAuthentication"));
    }

    @Test
    void getRequestToContactEndpoint_withSuccessParamAndUnauthenticatedUser_shouldReturnContactViewWithSuccessMessage() throws Exception {
        MockHttpServletRequestBuilder request = get("/contact").param("success", "true");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactShopRequest"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("successMessage", "Your message has been sent successfully. We will get back to you soon."))
                .andExpect(model().attributeDoesNotExist("user"))
                .andExpect(model().attributeDoesNotExist("userAuthentication"));
    }

    @Test
    void postRequestToContactEndpoint_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/contact")
                .formField("name", "Mimozi")
                .formField("email", "test@test.com")
                .formField("subject", "test")
                .formField("body", "My test")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact?success=true"));
        verify(emailService, times(1)).sendEmailThroughContactForm(any());
    }

    @Test
    void postRequestToContactEndpointwhenInvalidDataProvided_thenContactView() throws Exception {
        MockHttpServletRequestBuilder request = post("/contact")
                .formField("name", "")
                .formField("email", "test@test.com")
                .formField("subject", "")
                .formField("body", "My ")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
        verify(emailService, never()).sendEmailThroughContactForm(any());
    }

    @Test
    @WithMockUser(username = "test-user")
    void getRequestToFaqEndpointAsAuthenticatedUser_shouldReturnFaqViewWithUserAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/faq");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("faq"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void getRequestToFaqEndpointAsUnauthenticatedUser_shouldReturnFaqViewWithoutUserAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/faq");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("faq"))
                .andExpect(model().attributeDoesNotExist("user"));
    }
}
