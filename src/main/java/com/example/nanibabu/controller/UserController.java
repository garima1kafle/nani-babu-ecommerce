package com.example.nanibabu.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nanibabu.entity.User;
import com.example.nanibabu.service.UserService;
import jakarta.validation.Valid;
@Controller

public class UserController {
	// A temporary list to store registered users
	private List<User> registeredUsers = new ArrayList<>();
	int id = 1;
	
	@Autowired
	private UserService userService;
	// Home page
	@GetMapping("/home")
	public String home() {
		return "e-commerce/index"; // Returns the home page (index.html)
	}
	// Login page
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new User());
		return "e-commerce/login"; // Returns the login page (login.html)
	}
	// Handling login post request
	@PostMapping("/login")
	public String login(@ModelAttribute("user") User user, Model model) {
//	    if (userService.checkLogin(user)) {
//	        return "redirect:/home"; // Redirect to welcome page on successful login
//	    } else {
//	        model.addAttribute("error", "Invalid username or password");
//	        return "e-commerce/login"; // Return to login page with error
//	    }
		
		if (SecurityContextHolder.getContext().getAuthentication() != null
		&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
	return "redirect:/home";
	}
		return "login";
	}

	// Register page
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("totalUsers", registeredUsers.size());
		return "e-commerce/register"; // Returns the register page (register.html)
	}
	// Handling registration post request
	@PostMapping("/register")
	public String processRegister(@Valid @ModelAttribute("user") User user, BindingResult result) {
	    if (result.hasErrors()) {
	        return "register"; // Handle errors
	    }
	    userService.saveUser(user); // Save user
	    System.out.println("User saved: " + user.getUsername());
	    return "redirect:/user-details"; // Redirect after registration
	}

	
	@GetMapping("/user-details")
	public String index(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("registeredUsers", users);
		return "e-commerce/registration_details";
	}
	// Submit registration page (shows registered user details)
	@PostMapping("/registration_details")
	public String submitRegistration(Model model, User user) {
		user.setId(id);
		id++;
		registeredUsers.add(user);
		return "redirect:/user-details"; // Displays the table of registered users
	}
	// Edit user
	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable int id, Model model) {
	    User userToEdit = userService.getAllUsers().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
	    if (userToEdit != null) {
	        model.addAttribute("user", userToEdit);
	        return "e-commerce/edit_user"; // View for editing user
	    }
	    return "redirect:/user-details"; // Redirect if user not found
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable int id, @ModelAttribute("user") User user) {
	    User existingUser = userService.getUserById(id);
	    if (existingUser != null) {
	        existingUser.setFirstName(user.getFirstName());
	        existingUser.setLastName(user.getLastName());
	        existingUser.setUsername(user.getUsername());
	        existingUser.setEmail(user.getEmail());

	        // Only update password if a new password is provided
	        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
	            existingUser.setPassword(user.getPassword());
	        }

	        userService.saveUser(existingUser);
	    }
	    return "redirect:/user-details"; // Redirect to user details page
	}

	
	// Delete user
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable int id) {
	    userService.deleteUserById(id); // Use UserService to delete the user
	    return "redirect:/user-details"; // Redirect back to the list after deletion
	}

}
