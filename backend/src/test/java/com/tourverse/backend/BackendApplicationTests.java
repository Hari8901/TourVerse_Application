package com.tourverse.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tourverse.backend.auth.service.EmailService;
import com.tourverse.backend.auth.service.OtpService;
import com.tourverse.backend.user.dto.TravelerLoginRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private OtpService otpService;

	@MockitoBean
	private EmailService emailService;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void testGetAllTourPackagesEndpoint() throws Exception {
		mockMvc.perform(get("/api/public/tours")).andExpect(status().isOk());
	}

	@Test
	@Order(3)
	void testTravelerRegistrationInitiation() throws Exception {
		// Mock the services to prevent real external calls
		Mockito.when(otpService.generateOtp(anyString())).thenReturn("123456");
		Mockito.doNothing().when(emailService).sendOtpEmail(anyString(), anyString(), anyString());

		MockMultipartFile profilePicture = new MockMultipartFile(
				"profilePicture",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);

		mockMvc.perform(multipart("/api/traveler/register/init")
						.file(profilePicture)
						.param("name", "Test Traveler")
						.param("email", "test.traveler@example.com")
						.param("phone", "1234567890")
						.param("password", "Password123"))
				.andExpect(status().isOk());
	}

	@Test
	@Order(4)
	void testTravelerLoginInitiation() throws Exception {
		// Mock the services to prevent real external calls
		Mockito.when(otpService.generateOtp(anyString())).thenReturn("123456");
		Mockito.doNothing().when(emailService).sendOtpEmail(anyString(), anyString(), anyString());

		TravelerLoginRequest loginRequest = TravelerLoginRequest.builder()
				.email("admin@tourverse.com")
				.password("admin123")
				.build();

		mockMvc.perform(post("/api/traveler/login/init")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isAccepted());
	}

	@Test
	@Order(5)
	void testPublicGuideSearch() throws Exception {
		mockMvc.perform(get("/api/public/guides/search")
						.param("date", "2025-12-25")
						.param("location", "Pune")
						.param("language", "English"))
				.andExpect(status().isOk());
	}
}