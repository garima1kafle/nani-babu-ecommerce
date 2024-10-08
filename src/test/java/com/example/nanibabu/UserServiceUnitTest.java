package com.example.nanibabu;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.nanibabu.entity.User;
import com.example.nanibabu.repo.UserRepository;
import com.example.nanibabu.service.UserService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceUnitTest {
	@Mock
	UserRepository userRepo;

	@InjectMocks
	UserService userService;

	@Mock
	PasswordEncoder encoder;
	private Validator validator;

	@BeforeEach
	void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Mockito.when(userRepo.save(any())).thenReturn(null);
		List<User> userList = new ArrayList<>();
		User u1 = new User();
		u1.setFirstName("test user");
		userList.add(u1);
		Mockito.when(userRepo.findAll()).thenReturn(userList);
		Mockito.doNothing().when(userRepo).deleteById(any());
	}

	@Test
	void testInsertUser() {
		User u1 = new User();
		u1.setFirstName("Ram");
		u1.setLastName("Sth");
		u1.setPassword("ram");
		u1.setConfirmPassword("ram");
		u1.setEmail("ram@gmail.com");
		userService.saveUser(u1);
		assertThatNoException();
	}

	@Test
	void testInsertUserFailedValidation() {
		User u1 = new User();
		u1.setFirstName("Ram");
		u1.setLastName("Sth");
		u1.setPassword("ram");
		u1.setConfirmPassword("ram");
		u1.setEmail("ram@gmail.com");
//		User u = userService.addUser(u1);
		Set<ConstraintViolation<User>> violations = validator.validate(u1);

		assertTrue(violations.isEmpty());
//		assertThatNoException();
	}

//
	@Test
	void testFetchUserList() {
		List<User> userList = userService.getAllUsers();
		assertEquals(1, userList.size());

	}

}