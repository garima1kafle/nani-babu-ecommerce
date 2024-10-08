package com.example.nanibabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.example.nanibabu.entity.User;
import com.example.nanibabu.repo.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NaniBabuEcommerceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository repo;

	@BeforeEach
	public void setUp() {

		User u1 = new User();
		u1.setFirstName("Ram");
		u1.setLastName("Sth");
		u1.setUsername("ram");
		u1.setEmail("ram@gmail.com");
		u1.setPassword("ram");
		u1.setConfirmPassword("ram");
		repo.save(u1);

	}

	@AfterEach
	public void removeUsers() {
		repo.deleteAll();
	}

	@Test
	@WithMockUser(username = "ram")
	void testInsertMockRecord() throws Exception {

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
				.file("images", "This is a test file".getBytes()).param("username", "Hari").param("password", "hari")
				.param("email", "hari@gmail.com").param("firstName", "hari").
				param("lastName", "Stha").param("confirmPassword","hari")).andReturn();
		assertEquals(302, result.getResponse().getStatus());
		assertEquals("redirect:/user-details", result.getModelAndView().getViewName());

	}

	@Test
	@WithMockUser(username = "ram")
	void testInsertMockRecordValidationFailed() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
				.file("images", "This is a test file".getBytes()).param("username", "Hari").param("password", "hari")
				.param("email", "hari@gmail.com").param("firstName", "hari").
				param("lastName", "Stha").param("confirmPassword","hari"))
//			
				.andReturn();
		// Retrieve the model and assert the BindingResult contains errors
		ModelMap modelMap = result.getModelAndView().getModelMap();
//		assertTrue(modelMap.containsAttribute("org.springframework.validation.BindingResult.user"));

		// Retrieve BindingResult object from the model and assert on errors
		BindingResult bindingResult = (BindingResult) modelMap.get("org.springframework.validation.BindingResult.user");
//		assertTrue(bindingResult.hasErrors());

//		assertTrue(bindingResult.hasFieldErrors("email"));
//		assertEquals("Email is mandatory", bindingResult.getFieldError("email").getDefaultMessage());
	}

	@Test
	@WithMockUser(username = "ram")
	void testShowUserList() throws Exception {

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user-details")).andReturn();
		assertEquals(200, result.getResponse().getStatus());
		List<User> userList = (List<User>) result.getModelAndView().getModel().get("users");
//		assertNotNull(userList);
//		assertEquals(1, userList.size());

	}

}