package com.cinemanager.cinemanager_user_service.util.dataFormatsUtil;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmailFormatTestUtil {
    @Value("${error_messages.email_invalid_format}")
    private String emailInvalidFormat;

    @Value("${fields.email}")
    private String emailField;

     public void testInvalidEmailsShouldReturnBadRequest(String jsonRequest, String postBody, MockMvc mockMvc) throws Exception {
        String[] invalidEmails = new String[]{"@admin.com", "adminadmin.com", "admin@.com", "admin@admin."};
        for (String invalidEmail : invalidEmails) {
            mockMvc.perform(post(postBody)
                            .contentType("application/json")
                            .content(jsonRequest.replace("%s", invalidEmail)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.message").value(emailInvalidFormat))
                    .andExpect(jsonPath("$.error.field").value(emailField));
        }
    }

}