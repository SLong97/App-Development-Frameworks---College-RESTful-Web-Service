package ie.sean.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.sean.project.controllers.NewDepartment;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("ie.sean.project")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc()
class ProjectApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Order(1)
	@WithMockUser
	@Test
	void getAllOffices() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/offices"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.offices", Matchers.hasSize(12)));
	}

	@Order(2)
	@Test
	@WithMockUser(roles = "HOS")
	void deleteOffice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/offices/A123")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Order(3)
	@Test
	@WithMockUser(roles = "HOD")
	void deleteOfficeNotExist() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/offices/ABCD")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Order(4)
	@Test
	void deleteOfficeNoUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/offices/A123")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}



	@Order(5)
	@Test
	@WithMockUser(roles = "HOS")
	void postNewDepartment() throws Exception {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Science", "Science@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/departments")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$._links.self").exists());
	}

	@Order(6)
	@Test
	@WithMockUser(roles = "HOD") //HOD is forbidden
	void postNewDepartmentWrongUser() throws Exception {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Science", "Science@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/departments")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$._links.self").doesNotExist());
	}

	@Order(7)
	@Test
	void postNewDepartmentNoUser() throws Exception {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Science", "Science@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/departments")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$._links.self").doesNotExist());
	}

	@Order(8)
	@Test
	@WithMockUser(roles = "HOS")
	void postNewDepartmentConflict() throws Exception {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("ComputerScience", "ComputerScience@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/departments")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$._links.self").doesNotExist());
	}

	@Order(9)
	@Test
	@WithMockUser(roles = "HOS")
	void postNewDepartmentUnableToBind() throws Exception {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("", "ComputerScience@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/departments")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}
