package com.cinemanager.cinemanager_user_service.staff;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest
@AutoConfigureMockMvc
class StaffRegistrationTests {


	@Test
	void contextLoads() {}


	@Autowired
	private MockMvc mockMvc;


	private UserNameFormatTest userNameFormatTest;
	private EmailFormatTest emailFormatTest;
	private PasswordFormatTest passwordFormatTest;


	private static final String POST_REGISTER_STAFF = "/api/staff/register";

	@Value("${staff_role.admin}")
	private String staffRoleAdmin;
	@Value("${staff_role.supervisor}")
	private String staffRoleSupervisor;
	@Value("${staff_role.employee}")
	private String staffRoleEmployee;

	@Value("${error_messages.user_role_not_found}")
	private String staffRoleNotFound;
	@Value("${error_messages.missing_fields}")
	private String missingFields;
	@Value("${error_messages.name_already_exists}")
	private String nameAlreadyExists;
 	@Value("${error_messages.email_already_exists}")
	private String emailAlreadyExists;

	@Value("${fields.user_role}")
	private String userRoleField;

	@Value("${fields_values_tests.name}")
	private String nameValue;
	@Value("${fields_values_tests.password}")
	private String passwordValue;
	@Value("${fields_values_tests.email}")
	private String emailValue;





	// Formats tests
	@Test
    public void testNameInvalidFormat() throws Exception {
		String jsonRequestFirstName =  "{\"name\":\"%s\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleAdmin + "\"}";
		String jsonRequestLastName =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"%s\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		userNameFormatTest.testInvalidNamesShouldReturnBadRequest(jsonRequestFirstName, POST_REGISTER_STAFF, mockMvc);
		userNameFormatTest.testInvalidNamesShouldReturnBadRequest(jsonRequestLastName, POST_REGISTER_STAFF, mockMvc);
	}

	@Test
    public void testEmailInvalidFormats() throws Exception {
		String jsonRequest =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"%s\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		emailFormatTest.testInvalidEmailsShouldReturnBadRequest(jsonRequest, POST_REGISTER_STAFF, mockMvc);
	}

	@Test
    public void testPasswordInvalidFormat() throws Exception {
		String jsonRequest =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"%s\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		passwordFormatTest.testInvalidPasswordsShouldReturnBadRequest(jsonRequest, POST_REGISTER_STAFF, mockMvc);
	}


	// Registration By Role Tests
	@Test
    public void testShouldRegisterStaffEmployee() throws Exception {
		String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleEmployee + "\"}";

		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value(nameValue))
				.andExpect(jsonPath("$.last_name").value(nameValue))
				.andExpect(jsonPath("$.email").value(emailValue))
				.andExpect(jsonPath("$.user_role").value(staffRoleEmployee));

		// Check in database
		DataUtil dataUtil = new DataUtil();
		assert dataUtil.checkStaffEmployeeExists(jsonPath("$.id").value(String.class).toString());
	}

	@Test
    public void testShouldRegisterStaffSupervisor() throws Exception {
		String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleSupervisor + "\"}";

		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value(nameValue))
				.andExpect(jsonPath("$.last_name").value(nameValue))
				.andExpect(jsonPath("$.email").value(emailValue))
				.andExpect(jsonPath("$.user_role").value(staffRoleSupervisor));

		// Check in database
		DataUtil dataUtil = new DataUtil();
		assert dataUtil.checkStaffSupervisorExists(jsonPath("$.id").value(String.class).toString());
	}

	@Test
    public void testShouldRegisterStaffAdmin() throws Exception {
		String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists()) 
				.andExpect(jsonPath("$.name").value(nameValue))
				.andExpect(jsonPath("$.last_name").value(nameValue))
				.andExpect(jsonPath("$.email").value(emailValue))
				.andExpect(jsonPath("$.user_role").value(staffRoleAdmin));

		// Check in database
		DataUtil dataUtil = new DataUtil();
		assert dataUtil.checkStaffAdminExists(jsonPath("$.id").value(String.class).toString());
	}

	@Test
    public void testShouldNotRegisterStaffByInvalidRole() throws Exception {
		String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"INVALID_ROLE\"}";

		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.message").value(staffRoleNotFound))
				.andExpect(jsonPath("$.error.field").value(userRoleField));
	}


	// Fields tests
	@Test
    public void testMissingFieldsRegisterStaff() throws Exception {
		String jsonRequest =  "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\"}"; // missing email and user_role
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.message").value(missingFields))
				.andExpect(jsonPath("$.error.missing_fields").isArray())
				.andExpect(jsonPath("$.error.missing_fields").value(hasItems("email", "user_role")));
	}

	@Test
    public void testNameAlreadyExistsRegisterStaff() throws Exception {
		DataUtil dataUtil = new DataUtil();
		dataUtil.createStaffAdminIntoDatabase();

		String jsonRequest = "{\"name\":\"" + nameValue + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"different@email.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";


		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error.message").value(nameAlreadyExists));
	}

	@Test
    public void testEmailAlreadyExistsRegisterStaff() throws Exception {
		DataUtil dataUtil = new DataUtil();
		dataUtil.createStaffAdminIntoDatabase();

		String jsonRequest = "{\"name\":\"" + "differentName" + "\", \"last_name\":\"" + nameValue + "\", \"password\":\"" + passwordValue + "\",\"email\":\"" + emailValue + "\",\"user_role\":\"" + staffRoleAdmin + "\"}";
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error.message").value(emailAlreadyExists));

	}


}
