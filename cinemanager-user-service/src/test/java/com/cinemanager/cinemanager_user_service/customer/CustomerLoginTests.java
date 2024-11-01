package com.cinemanager.cinemanager_user_service.customer;

import com.cinemanager.cinemanager_user_service.util.DataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerLoginTests {

    @Test
    void contextLoads() {}


    @Autowired
    private MockMvc mockMvc;

    private static final String POST_LOGIN = "/api/customer/login";

    @Value("${error_messages.user_not_found.email}")
    private String userNotFoundEmail;
    @Value("${error_messages.user_not_found.password}")
    private String userNotFoundPassword;

    @Value("${fields.email}")
    private String emailField;
    @Value("${fields.password}")
    private String passwordField;

    @Value("${fields_values_tests.name}")
    private String nameValue;
    @Value("${fields_values_tests.password}")
    private String passwordValue;
    @Value("${fields_values_tests.email}")
    private String emailValue;


    @BeforeAll
    static void setUp() {
        DataUtil dataUtil = new DataUtil();
        dataUtil.createCustomerIntoDatabase();
    }

    @Test
    void testShouldLoginCustomer() throws Exception {
        String jsonRequest = "{\"email\":\"" + emailValue + "\",\"password\":\"" + passwordValue + "\"}";
        mockMvc.perform(post(POST_LOGIN)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(nameValue))
                .andExpect(jsonPath("$.last_name").value(nameValue))
                .andExpect(jsonPath("$.email").value(emailValue));

    }

    @Test
    void testShouldNotLoginCustomerByInvalidEmail() throws Exception {
        String jsonRequest = "{\"email\":\"notexist@email.com\",\"password\":\"" + passwordValue + "\"}";
        mockMvc.perform(post(POST_LOGIN)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(userNotFoundEmail))
                .andExpect(jsonPath("$.error.field").value(emailField));
    }

    @Test
    void testShouldNotLoginCustomerByInvalidPassword() throws Exception {
        String jsonRequest = "{\"email\":\"" + emailValue + "\",\"password\":\"incorrectpassword\"}";
        mockMvc.perform(post(POST_LOGIN)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(userNotFoundPassword))
                .andExpect(jsonPath("$.error.field").value(passwordField));
    }

}
