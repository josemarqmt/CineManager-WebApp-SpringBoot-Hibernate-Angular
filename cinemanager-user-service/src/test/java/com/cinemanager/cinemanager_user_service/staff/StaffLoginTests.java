package com.cinemanager.cinemanager_user_service.staff;

import com.cinemanager.cinemanager_user_service.util.DataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class StaffLoginTests {

    @Test
    void contextLoads() {}


    @Autowired
    private MockMvc mockMvc;

    private static final String POST_LOGIN = "/api/staff/login";

    @Value("${error_messages.user_not_found.email}")
    private String userNotFoundEmail;
    @Value("${error_messages.user_not_found.password}")
    private String userNotFoundPassword;


    @Value("${fields.email}")
    private String emailField;
    @Value("${fields.password}")
    private String passwordField;

    @Value("${staff_role.admin")
    private String staffAdmin;

    @Value("${fields_values_tests.name}")
    private String nameValue;
    @Value("${fields_values_tests.password}")
    private String passwordValue;
    @Value("${fields_values_tests.email}")
    private String emailValue;

    @BeforeAll
    static void setUp() {
        DataUtil dataUtil = new DataUtil();
        dataUtil.createStaffAdminIntoDatabase();
    }

    @Test
    void testShouldLoginStaff() throws Exception {
        String jsonRequest = "{\"email\":\"" + emailValue + "\",\"password\":\"" + passwordValue + "\"}";
        mockMvc.perform(post(POST_LOGIN)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(nameValue))
                .andExpect(jsonPath("$.last_name").value(nameValue))
                .andExpect(jsonPath("$.email").value(emailValue))
                .andExpect(jsonPath("$.user_role").value(staffAdmin));
    }

    @Test
    void testShouldNotLoginStaffByInvalidEmail() throws Exception {
        String jsonRequest = "{\"email\":\"notexist@email.com\",\"password\":\"" + passwordValue + "\"}";
        mockMvc.perform(post(POST_LOGIN)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(userNotFoundEmail))
                .andExpect(jsonPath("$.error.field").value(emailField));
    }

    @Test
    void testShouldNotLoginStaffByInvalidPassword() throws Exception {
        String jsonRequest = "{\"email\":\"" + emailValue + "\",\"password\":\"incorrectpassword\"}";
        mockMvc.perform(post(POST_LOGIN)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(userNotFoundPassword))
                .andExpect(jsonPath("$.error.field").value(passwordField));
    }
}
