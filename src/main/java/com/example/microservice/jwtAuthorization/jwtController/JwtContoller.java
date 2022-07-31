package com.example.microservice.jwtAuthorization.jwtController;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		Map<String , Object> responseBody = new HashMap<String , Object>();
		JsonReader jsr = Json.createReader(new StringReader(user));
		JsonObject jso = jsr.readObject();
		jsr.close();
		
		responseBody = jwtService.createOrUpdateUser(jso);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> checkUserValidity(){
		return ResponseEntity.ok(null);
	}
}
