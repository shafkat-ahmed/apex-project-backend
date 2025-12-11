package com.apex.template;

import com.apex.template.domain.dto.TaskDto;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


import java.time.LocalDate;
import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TemplateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	private String accessToken;
	private String refreshToken;

	@Value("${spring.client.id}")
	private String clientId;
	@Value("${spring.client.secret}")
	private String clientSecret;

	@Before
	public void setup() {
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}


	private String basicAuth() {
		return "Basic " + Base64.getEncoder()
				.encodeToString((clientId + ":" + clientSecret).getBytes());
	}

	// ============================================================
	//                   AUTH HELPER METHODS
	// ============================================================
	private String obtainAccessToken(String username, String password) throws Exception {
		MvcResult result = mockMvc.perform(post("/oauth/token")
						.header("Authorization", basicAuth())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("grant_type", "password")
						.param("username", username)           // must match DB user
						.param("password", password))           // must match DB password
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").exists())
				.andExpect(jsonPath("$.refresh_token").exists())
				.andExpect(jsonPath("$.token_type").value("bearer"))
				.andExpect(jsonPath("$.expires_in").exists())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		this.accessToken = JsonPath.read(response, "$.access_token");
		this.refreshToken = JsonPath.read(response, "$.refresh_token");

		return this.accessToken;
	}

	private String refreshAccessToken() throws Exception {
		MvcResult result = mockMvc.perform(post("/oauth/token")
						.param("grant_type", "refresh_token")
						.param("refresh_token", this.refreshToken)
						.header("Authorization", basicAuth())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				)
				.andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		this.accessToken = JsonPath.read(response, "$.access_token");

		return this.accessToken;
	}



	// ============================================================
	//                   TEST: LOGIN
	// ============================================================
	@Test
	public void testLogin() throws Exception {
		String token = obtainAccessToken("apex@gmail.com", "00000000");
		assertNotNull(token);
	}

	// ============================================================
	//            TEST: REFRESH TOKEN
	// ============================================================
	@Test
	public void testRefreshToken() throws Exception {
		obtainAccessToken("apex@gmail.com", "00000000");
		String newToken = refreshAccessToken();

		assertNotNull(newToken);
	}

	// ============================================================
	//                   TASK CRUD TESTS
	// ============================================================

	@Test
	public void testCreateTask() throws Exception {
		obtainAccessToken("apex@gmail.com", "00000000");

		TaskDto dto = new TaskDto();
		dto.setTitle("Task A");
		dto.setDescription("Desc A");
		dto.setStartDate(LocalDate.of(2020, 1, 1));
		dto.setDueDate(LocalDate.of(2020, 1, 2));
//		dto.setStatus("NEW");
		dto.setPriority("Low");

		mockMvc.perform(post("/api/task")
						.header("Authorization", "Bearer " + accessToken)
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title", is("Task A")));
	}

	@Test
	public void testGetAllTasks() throws Exception {
		obtainAccessToken("apex@gmail.com", "00000000");

		mockMvc.perform(get("/api/task")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetTaskById() throws Exception {
		obtainAccessToken("apex@gmail.com", "00000000");

		// First create one
		TaskDto dto = new TaskDto();
		dto.setTitle("Task B");
		dto.setStartDate(LocalDate.of(2020, 1, 1));
		dto.setDueDate(LocalDate.of(2020, 1, 2));
		dto.setPriority("Low");

		MvcResult result = mockMvc.perform(post("/api/task")
						.header("Authorization", "Bearer " + accessToken)
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		Long taskId =  ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();

		mockMvc.perform(get("/api/task/" + taskId)
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(taskId.intValue())));
	}

	@Test
	public void testUpdateTask() throws Exception {
		obtainAccessToken("apex@gmail.com", "00000000");

		// Create first
		TaskDto dto = new TaskDto();
		dto.setTitle("Old title");
		dto.setPriority("Low");
		dto.setStartDate(LocalDate.of(2020, 1, 1));
		dto.setDueDate(LocalDate.of(2020, 1, 2));

		MvcResult result = mockMvc.perform(post("/api/task")
						.header("Authorization", "Bearer " + accessToken)
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		Long taskId =  ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();

		// Update
		dto.setId(taskId);
		dto.setTitle("Updated title");

		mockMvc.perform(put("/api/task")
						.header("Authorization", "Bearer " + accessToken)
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title", is("Updated title")));
	}

	@Test
	public void testDeleteTask() throws Exception {
		obtainAccessToken("apex@gmail.com", "00000000");

		// Create first
		TaskDto dto = new TaskDto();
		dto.setTitle("ToDelete");
		dto.setStartDate(LocalDate.of(2020, 1, 1));
		dto.setDueDate(LocalDate.of(2020, 1, 2));
		dto.setPriority("Low");

		MvcResult result = mockMvc.perform(post("/api/task")
						.header("Authorization", "Bearer " + accessToken)
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		Long taskId =  ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();


		// Delete
		mockMvc.perform(delete("/api/task/" + taskId)
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNoContent());
	}
}

