package com.cinemanager.cinemanager_user_service.dataFormats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PasswordFormatTest { ;
    @Value("${error_messages.password_invalid_format.at_least_one_special_character}")
    private String passwordNoSpecialCharacter;
    @Value("${error_messages.password_invalid_format.at_least_one_lowercase}")
    private String passwordNoLowercase;
    @Value("${error_messages.password_invalid_format.at_least_one_uppercase}")
    private String passwordNoUppercase;
    @Value("${error_messages.password_invalid_format.at_least_one_number}")
    private String passwordNoNumber;
    @Value("${error_messages.password_invalid_format.more_than_thirty}")
    private String passwordMoreThanThirty;
    @Value("${error_messages.password_invalid_format.less_than_eight}")
    private String passwordLessThanEight;

    @Value("${fields.password}")
    private static String passwordField;

    public void testInvalidPasswordsShouldReturnBadRequest(String jsonRequest, String postBody, MockMvc mockMvc) throws Exception {
        Map<String, String> invalidPasswords = Map.of(
                "aA1aaaaaaa", passwordNoSpecialCharacter,
                "@A1AAAAAAA", passwordNoLowercase,
                "@a1aaaaaaa", passwordNoUppercase,
                "@aAaaaaaaa", passwordNoNumber,
                "@aA1aaaaaaaaaa", passwordMoreThanThirty,
                "@aA1aaa", passwordLessThanEight
        );

        for (Map.Entry<String, String> invalidPassword : invalidPasswords.entrySet()) {
            mockMvc.perform(post(postBody)
                            .contentType("application/json")
                            .content(jsonRequest.replace("%s", invalidPassword.getKey())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.message").value(invalidPassword.getValue()))
                    .andExpect(jsonPath("$.error.field").value(passwordField));
        }
    }
}
