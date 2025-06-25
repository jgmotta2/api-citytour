package br.edu.atitus.api_citytour.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.api_citytour.components.JWTUtils;
import br.edu.atitus.api_citytour.dtos.SigninDTO;
import br.edu.atitus.api_citytour.dtos.SignupDTO;
import br.edu.atitus.api_citytour.entities.UserEntity;
import br.edu.atitus.api_citytour.entities.UserType;
import br.edu.atitus.api_citytour.services.UserService;

@RestController
@RequestMapping("/auth")
public class 	AuthController {

	private final UserService service;
	private final AuthenticationConfiguration authConfig;
	private final JWTUtils jwtUtils;

	public AuthController(UserService service, AuthenticationConfiguration authConfig, JWTUtils jwtUtils) {
		super();
		this.service = service;
		this.authConfig = authConfig;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody SigninDTO signin) {
		try {
			authConfig.getAuthenticationManager()
					.authenticate(new UsernamePasswordAuthenticationToken(signin.email(), signin.password()));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

		} catch (Exception e) {

			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok(jwtUtils.generateToken(signin.email()));
	}


	@PostMapping("/signup")
	public ResponseEntity<UserEntity> signup(@RequestBody SignupDTO dto) throws Exception{
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(dto, user);
		user.setType(UserType.Common);

		service.save(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
}