package br.edu.atitus.api_citytour.services;

import br.edu.atitus.api_citytour.components.InvalidPasswordException;
import br.edu.atitus.api_citytour.components.ResourceNotFoundExcep;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_citytour.entities.UserEntity;
import br.edu.atitus.api_citytour.repositories.UserRepository;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	private static final java.util.regex.Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static final String PASSWORD_STRENGTH_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
	private static final Pattern PASSWORD_STRENGTH_PATTERN = Pattern.compile(PASSWORD_STRENGTH_REGEX);


	public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserEntity updateProfile(UUID userId, UserEntity updatedUser) throws ResourceNotFoundExcep {
		UserEntity existingUser = repository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundExcep("User not found."));

		existingUser.setName(updatedUser.getName().trim());


		return repository.save(existingUser);
	}

	public void updatePassword(UserEntity user, String oldPassword, String newPassword) throws Exception {
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new Exception("Incorrect old password.");
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		repository.save(user);
	}

	public UserEntity save(UserEntity user) throws ResourceNotFoundExcep, InvalidPasswordException {
		if (user == null)
			throw new ResourceNotFoundExcep("Object cannot be null.");

		if (user.getName() == null || user.getName().isEmpty())
			throw new ResourceNotFoundExcep("Invalid name.");
		user.setName(user.getName().trim());


		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new ResourceNotFoundExcep("Invalid email.");

		Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());
		if (!matcher.matches()) {
			throw new ResourceNotFoundExcep("Invalid email format.");
		}
		user.setEmail(user.getEmail().trim().toLowerCase());

		if (user.getPassword() == null
				|| user.getPassword().isEmpty()
				|| user.getPassword().length() < 8)
			throw new InvalidPasswordException("Invalid password.");

		Matcher passwordMatcher = PASSWORD_STRENGTH_PATTERN.matcher(user.getPassword());
		if (!passwordMatcher.matches()) {
			throw new InvalidPasswordException("Invalid password: must contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
		}

		if (user.getType() == null)
			throw new ResourceNotFoundExcep("Invalid user type.");

		if (repository.existsByEmail(user.getEmail()))
			throw new ResourceNotFoundExcep("User with this email already exists.");

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		repository.save(user);

		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return user;
	}

}