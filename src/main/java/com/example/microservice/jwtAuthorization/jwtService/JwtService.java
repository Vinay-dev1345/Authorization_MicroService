package com.example.microservice.jwtAuthorization.jwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservice.jwtAuthorization.jwtUserRepository.UserRepository;

@Service
public class JwtService {
	
	@Autowired
	UserRepository userRepository;
	
	public void createOrUpdateUser() {
		
	}
	
}
