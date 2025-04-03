package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.Role;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerApiTest {

    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private View view;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRequestToAdminPanel_withAdminRole_shouldReturnAdminView() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }

    @Test
    void getRequestToAddProductEndpoint_shouldReturnAddProductView() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/add-product");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-product"))
                .andExpect(model().attributeExists("addProductRequest", "addProductRequest"));
    }

    @Test
    void getRequestToUsersEndpoint_shouldReturnUsersView() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/users");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("allUsers", "allUsers"));
    }

    @Test
    void getRequestToInactiveProductsEndpoint_shouldReturnInactiveProductsView() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/products/inactive");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("inactive-products"))
                .andExpect(model().attributeExists("inactiveProducts", "inactiveProducts"));
    }

    @Test
    void getRequestToContactFormEmailsEndpoint_shouldReturnContactFormEmailsView() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/contact-form-emails");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("contact-form-emails"))
                .andExpect(model().attributeExists("allContactFormEmails", "allContactFormEmails"));
    }

    @Test
    void putUnauthorisedRequestToSwitchRole_shouldReturn300AndRedirectToUsersPage() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "Username",
                "123123",
                Role.USER);
        MockHttpServletRequestBuilder request = put("/admin/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/users"));
    }

    @Test
    void putAuthorisedRequestToSwitchRole_shouldReturn300AndRedirectToUsersPage() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "Username",
                "123123",
                Role.ADMIN);
        MockHttpServletRequestBuilder request = put("/admin/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/users"));
        verify(userService, times(1)).switchRole(any());
    }

    @Test
    void putAuthorisedRequestToActivateProduct_shouldReturn300AndRedirectToProductsPage() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "Username",
                "123123",
                Role.ADMIN);
        MockHttpServletRequestBuilder request = put("/admin/product/activate/{id}", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/products"));
        verify(productService, times(1)).activateProduct(any());
    }

    @Test
    void putUnauthorisedRequestToActivateProduct_shouldReturn300AndRedirectToProductsPage() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "Username",
                "123123",
                Role.USER);
        MockHttpServletRequestBuilder request = put("/admin/product/activate/{id}", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/products"));
    }

    @Test
    void putUnauthorisedRequestToDeleteProduct_shouldReturn300AndRedirectToProductsPage() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "Username",
                "123123",
                Role.USER);
        MockHttpServletRequestBuilder request = put("/admin/product/delete/{id}", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/products"));
    }

    @Test
    void putAuthorisedRequestToDeleteProduct_shouldReturn300AndRedirectToProductsPage() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "Username",
                "123123",
                Role.ADMIN);
        MockHttpServletRequestBuilder request = put("/admin/product/delete/{id}", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/products"));
        verify(productService, times(1)).deactivateProductById(any());
    }


}
