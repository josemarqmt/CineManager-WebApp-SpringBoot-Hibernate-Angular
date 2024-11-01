package com.cinemanager.cinemanager_user_service.customer;

import com.cinemanager.cinemanager_user_service.dataFormats.EmailFormatTest;
import com.cinemanager.cinemanager_user_service.dataFormats.PasswordFormatTest;
import com.cinemanager.cinemanager_user_service.dataFormats.UserNameFormatTest;
import com.cinemanager.cinemanager_user_service.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerRegistrationTests {

    @Test
    void contextLoads() {}


    @Autowired
    private MockMvc mockMvc;


    private UserNameFormatTest userNameFormatTest;
    private EmailFormatTest emailFormatTest;
    private PasswordFormatTest passwordFormatTest;


    private static final String POST_REGISTER_CUSTOMER = "/api/customer/register";


    @Value("${error_messages.missing_fields}")
    private String missingFields;
    @Value("${error_messages.name_already_exists}")
    private String nameAlreadyExists;
    @Value("${error_messages.email_already_exists}")
    private String emailAlreadyExists;

    @Value("${fields_values_tests.name}")
    private String nameValue;
    @Value("${fields_values_tests.password}")
    private String passwordValue;
    @Value("${fields_values_tests.email}")
    private String emailValue;


    // Formats tests
    @Test
    public void testNameInvalidFormat() throws Exception {
        String jsonRequestFirstName =  "{\"name\":\"%s\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\"}";

        userNameFormatTest.testInvalidNamesShouldReturnBadRequest(jsonRequestFirstName, POST_REGISTER_CUSTOMER, mockMvc);
    }

    @Test
    public void testEmailInvalidFormats() throws Exception {
        String jsonRequest =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"%s\"}";

        emailFormatTest.testInvalidEmailsShouldReturnBadRequest(jsonRequest, POST_REGISTER_CUSTOMER, mockMvc);
    }

    @Test
    public void testPasswordInvalidFormat() throws Exception {
        String jsonRequest =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"%s\",\"email\":\"" + emailValue + "\"}";

        passwordFormatTest.testInvalidPasswordsShouldReturnBadRequest(jsonRequest, POST_REGISTER_CUSTOMER, mockMvc);
    }

    // Fields tests
    @Test
    public void testMissingFieldsRegisterCustomer() throws Exception {
        String jsonRequest =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\"}"; // missing email and user_role
        mockMvc.perform(post("/api/customer/register")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(missingFields))
                .andExpect(jsonPath("$.error.missing_fields").isArray())
                .andExpect(jsonPath("$.error.missing_fields").value(hasItems("email", "user_role")));
    }

    @Test
    public void testNameAlreadyExistsRegisterCustomer() throws Exception {
        DataUtil dataUtil = new DataUtil();
        dataUtil.createCustomerIntoDatabase();

        String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"different@email.com\"}";

        mockMvc.perform(post("/api/customer/register")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.message").value(nameAlreadyExists));
    }

    @Test
    public void testEmailAlreadyExistsRegisterCustomer() throws Exception {
        DataUtil dataUtil = new DataUtil();
        dataUtil.createCustomerIntoDatabase();

        String jsonRequest = "{\"name\":\"" + "differentName" + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\"}";
        mockMvc.perform(post("/api/customer/register")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.message").value(emailAlreadyExists));

    }

    // registration test
    @Test
    public void testShouldRegisterCustomer() throws Exception {
        String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\"}";

        mockMvc.perform(post("/api/customer/register")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(nameValue))
                .andExpect(jsonPath("$.last_name").value(nameValue))
                .andExpect(jsonPath("$.email").value(emailValue));

        // Check in database
        DataUtil dataUtil = new DataUtil();
        assert dataUtil.checkCustomerExists(jsonPath("$.id").value(String.class).toString());
    }

}
