package com.example.nanibabu.service;

import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.nanibabu.entity.User;
import com.example.nanibabu.repo.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Use the password encoder bean
    
//  private Logger logger = LoggerFactory.getLogger(getClass());
//
//	public UserService() {
//		logger.info("UserService created ");
//}

    // Method to register a user
//    public void saveUser(User user) {
//        // Only encode password if it's not null or empty
//        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        }
//        userRepository.save(user);
//    }


	public User saveUser(User user) {
		System.out.println("encoding user password");
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		System.out.println("encoding user password done");
		return userRepository.save(user);
	}

    // Method to check login credentials
    public boolean checkLogin(User user) {
        User registeredUser = userRepository.findByUsername(user.getUsername());
        if (registeredUser == null) {
            System.out.println("User not found: " + user.getUsername());
            return false;
        }
        boolean matches = passwordEncoder.matches(user.getPassword(), registeredUser.getPassword());
        System.out.println("Login check for " + user.getUsername() + ": " + matches);
        return matches;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
    
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null); // Fetch user by ID
    }
    
   

}
