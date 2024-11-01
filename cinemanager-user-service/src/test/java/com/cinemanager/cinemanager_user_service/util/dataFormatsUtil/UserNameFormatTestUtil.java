package com.cinemanager.cinemanager_user_service.util.dataFormatsUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserNameFormatTestUtil {
    @Value("${error_messages.name_invalid_format.alphanumeric}")
    private String nameNotAlphanumeric;
    @Value("${error_messages.name_invalid_format.between_3_and_20_characters}")
    private String nameLengthLessThanThreeAndMoreThanTwenty;
    @Value("${fields.name}")
    private String nameField;

    public void testInvalidNamesShouldReturnBadRequest(String jsonRequest, String postBody, MockMvc mockMvc) throws Exception {
        Map<String, String> invalidNames = Map.of(
                "a@b", nameNotAlphanumeric,
                "a".repeat(21), nameLengthLessThanThreeAndMoreThanTwenty,
                "aa", nameLengthLessThanThreeAndMoreThanTwenty
        );

        for (Map.Entry<String, String> invalidName : invalidNames.entrySet()) {
            mockMvc.perform(post(postBody)
                            .contentType("application/json")
                            .content(jsonRequest.replace("%s", invalidName.getKey())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.message").value(invalidName.getValue()))
                    .andExpect(jsonPath("$.error.field").value(nameField));
        }
    }

}
