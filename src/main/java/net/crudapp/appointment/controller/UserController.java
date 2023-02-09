package net.crudapp.appointment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.crudapp.appointment.exception.ResourceNotFoundException;
import net.crudapp.appointment.jwt.JwtUtils;
import net.crudapp.appointment.model.UserModel;
import net.crudapp.appointment.payload.JwtResponse;
import net.crudapp.appointment.payload.LoginRequest;
import net.crudapp.appointment.payload.MessageResponse;
import net.crudapp.appointment.payload.SignUpRequest;
import net.crudapp.appointment.repository.UserRepository;
import net.crudapp.appointment.services.UserDetailsImpl;

@RestController
@RequestMapping("/user/")
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;

	// getAll
	@GetMapping("/all")
	public List<UserModel> getAllUser() {
		return this.userRepository.findAll();
	}

	// getOneById
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> getUserById(@PathVariable(value = "id") Long UserId)
			throws ResourceNotFoundException {
		UserModel user = userRepository.findById(UserId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + UserId));
		return ResponseEntity.ok().body(user);
	}

	// login
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		UserModel user = new UserModel(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	// createUser
	@PostMapping("/create")
	public UserModel createUser(@Validated @RequestBody UserModel user) {
		this.userRepository.save(user);
		return this.userRepository.save(user);
	}

	// UpdateUser
	@PutMapping("/update/{id}")
	public ResponseEntity<UserModel> updateUser(@PathVariable(value = "id") Long userId,
			@Validated @RequestBody UserModel userDetails) throws ResourceNotFoundException {
		UserModel users = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + userId));
		users.setUsername(userDetails.getUsername());
		users.setEmail(userDetails.getEmail());
		users.setPassword(userDetails.getPassword());

		return ResponseEntity.ok(this.userRepository.save(users));
	}

	// DeleteUser
	@DeleteMapping("/delete/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		UserModel user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + userId));
		this.userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
