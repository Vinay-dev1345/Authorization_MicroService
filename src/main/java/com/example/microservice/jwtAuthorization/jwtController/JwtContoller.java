package com.example.microservice.jwtAuthorization.jwtController;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/user")
	public ResponseEntity<?> createUser(@RequestBody String user , HttpServletResponse response){
		Map<String , Object> responseBody = new HashMap<String , Object>();
		JsonReader jsr = Json.createReader(new StringReader(user));
		JsonObject jso = jsr.readObject();
		jsr.close();
		
		responseBody = jwtService.createOrUpdateUser(jso);
		if(!(boolean)responseBody.get("errors")) {
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
		}
		
	}
	
	@GetMapping("/user/{tokenId}")
	public ResponseEntity<?> checkUserValidity(@PathVariable String tokenId){
		Map<String , Object> responseBody = new HashMap<String , Object>();
		
		Boolean response = jwtService.isValiduser(tokenId);
		responseBody.put("isValid", response);
		return ResponseEntity.ok(responseBody);
	}
}
