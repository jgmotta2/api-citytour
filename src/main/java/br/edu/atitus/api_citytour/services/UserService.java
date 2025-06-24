package br.edu.atitus.api_citytour.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_citytour.entities.UserEntity;
import br.edu.atitus.api_citytour.repositories.UserRepository;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService{
	
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

	public UserEntity updateProfile(UUID userId, UserEntity updatedUser) throws Exception {
		UserEntity existingUser = repository.findById(userId)
				.orElseThrow(() -> new Exception("Usuário não encontrado."));

		existingUser.setName(updatedUser.getName().trim());
		existingUser.setBirthDate(updatedUser.getBirthDate());


		return repository.save(existingUser);
	}


	public UserEntity save(UserEntity user) throws Exception {
		if (user == null)
			throw new Exception("Objeto não pode ser nulo");
		
		if (user.getName() == null || user.getName().isEmpty())
			throw new Exception("Nome inválido");
		user.setName(user.getName().trim());
		
		
		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new Exception("E-mail inválido");

		Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());
		if (!matcher.matches()) {
			throw new Exception("Formato de e-mail inválido.");
		}
		user.setEmail(user.getEmail().trim().toLowerCase());

		if (user.getPassword() == null 
				|| user.getPassword().isEmpty()
				|| user.getPassword().length() < 8)
			throw new Exception("Password inválido");

		Matcher passwordMatcher = PASSWORD_STRENGTH_PATTERN.matcher(user.getPassword());
		if (!passwordMatcher.matches()) {
			throw new Exception("Senha inválida: deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.");
		}

		if (user.getBirthDate() == null) {
			throw new Exception("Data de nascimento é obrigatória.");
		}
		if (user.getBirthDate().isAfter(LocalDate.now())) {
			throw new Exception("Data de nascimento não pode ser no futuro.");
		}

		if (user.getType() == null)
			throw new Exception("Tipo de usuário inválido");

		if (repository.existsByEmail(user.getEmail()))
			throw new Exception("Já existe usuário cadastrado com este e-mail");

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		repository.save(user);
		
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		return user;
	}

}
