package com.cinemanager.cinemanager_user_service.staff;

import com.cinemanager.cinemanager_user_service.dataFormats.EmailFormatTest;
import com.cinemanager.cinemanager_user_service.dataFormats.PasswordFormatTest;
import com.cinemanager.cinemanager_user_service.dataFormats.UserNameFormatTest;
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

	@Value("${staff_role.error_messages.user_role_not_found}")
	private String staffRoleNotFound;
	@Value("${staff_role.error_messages.missing_fields}")
	private String missingFields;
	@Value("${staff_role.error_messages.name_already_exists}")
	private String nameAlreadyExists;
	@Value("${staff_role.error_messages.email_already_exists}")
	private String emailAlreadyExists;

	@Value("${staff_role.fields.user_role}")
	private String userRoleField;



	// Formats tests
	@Test
    public void testNameInvalidFormat() throws Exception {
		String jsonRequestFirstName =  "{\"name\":\"%s\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";
		String jsonRequestLastName =  "{\"name\":\"admin\", \"last_name\":\"%s\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		userNameFormatTest.testInvalidNamesShouldReturnBadRequest(jsonRequestFirstName, POST_REGISTER_STAFF, mockMvc);
		userNameFormatTest.testInvalidNamesShouldReturnBadRequest(jsonRequestLastName, POST_REGISTER_STAFF, mockMvc);
	}

	@Test
    public void testEmailInvalidFormats() throws Exception {
		String jsonRequest =  "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"%s\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		emailFormatTest.testInvalidEmailsShouldReturnBadRequest(jsonRequest, POST_REGISTER_STAFF, mockMvc);
	}

	@Test
    public void testPasswordInvalidFormat() throws Exception {
		String jsonRequest =  "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"%s\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		passwordFormatTest.testInvalidPasswordsShouldReturnBadRequest(jsonRequest, POST_REGISTER_STAFF, mockMvc);
	}


	// Registration By Role Tests
	@Test
    public void testShouldRegisterStaffEmployee() throws Exception {
		String jsonRequest = "{\"name\":\"employee\", \"last_name\":\"employee\", \"password\":\"employee\",\"email\":\"employee@employee.com\",\"user_role\":\"" + staffRoleEmployee + "\"}";
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("employee"))
				.andExpect(jsonPath("$.last_name").value("employee"))
				.andExpect(jsonPath("$.email").value("employee@employee.com"))
				.andExpect(jsonPath("$.user_role").value(staffRoleEmployee));
	}

	@Test
    public void testShouldRegisterStaffSupervisor() throws Exception {
		String jsonRequest = "{\"name\":\"supervisor\", \"last_name\":\"supervisor\", \"password\":\"supervisor\",\"email\":\"supervisor@supervisor.com\",\"user_role\":\"" + staffRoleSupervisor + "\"}";
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("supervisor"))
				.andExpect(jsonPath("$.last_name").value("supervisor"))
				.andExpect(jsonPath("$.email").value("supervisor@supervisor.com"))
				.andExpect(jsonPath("$.user_role").value(staffRoleSupervisor));
	}

	@Test
    public void testShouldRegisterStaffAdmin() throws Exception {
		String jsonRequest = "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists()) 
				.andExpect(jsonPath("$.name").value("admin"))
				.andExpect(jsonPath("$.last_name").value("admin"))
				.andExpect(jsonPath("$.email").value("admin@admin.com")) 
				.andExpect(jsonPath("$.user_role").value(staffRoleAdmin));
	}

	@Test
    public void testShouldNotRegisterStaffByInvalidRole() throws Exception {
		String jsonRequest = "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"INVALID_ROLE\"}";
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
		String jsonRequest = "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\"}"; // missing email and user_role
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
		String firstJsonRequest = "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";
		String secondJsonRequest = "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"admin2@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		// first registration
		mockMvc.perform(post("/api/staff/register")
				.contentType("application/json")
				.content(firstJsonRequest));

		// second registration
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(secondJsonRequest))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error.message").value(nameAlreadyExists));
	}

	@Test
    public void testEmailAlreadyExistsRegisterStaff() throws Exception {
		String firstJsonRequest = "{\"name\":\"admin\", \"last_name\":\"admin\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";
		String secondJsonRequest = "{\"name\":\"admin2\", \"last_name\":\"admin2\", \"password\":\"admin\",\"email\":\"admin@admin.com\",\"user_role\":\"" + staffRoleAdmin + "\"}";

		// first registration
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(firstJsonRequest));

		// second registration
		mockMvc.perform(post("/api/staff/register")
						.contentType("application/json")
						.content(secondJsonRequest))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error.message").value(emailAlreadyExists));
	}


}
