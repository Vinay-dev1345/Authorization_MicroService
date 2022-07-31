package com.example.microservice.jwtAuthorization.jwtController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.jwtAuthorization.jwtService.JwtService;

@RestController
@RequestMapping("v1/authorize")
public class JwtContoller {
	
	@Autowired
	JwtService jwtService;
	
	@PostMapping("/user")
	public ResponseEntity<?> createUser(@RequestBody String user){
		return null;
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> checkUserValidity(){
		return ResponseEntity.ok(null);
	}
}
