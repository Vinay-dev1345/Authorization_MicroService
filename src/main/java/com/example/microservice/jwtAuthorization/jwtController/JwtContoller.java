package com.example.microservice.jwtAuthorization.jwtController;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.jwtAuthorization.JwtAuthorizationApplication;
import com.example.microservice.jwtAuthorization.jwtService.JwtService;


@RestController
@RequestMapping("v1/authorize")
public class JwtContoller {
	private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationApplication.class);
	
	@Autowired
	JwtService jwtService;
	
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/user" , produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> createUser(@RequestBody String user , HttpServletResponse response ){
		Map<String , Object> responseBody = new HashMap<String , Object>();
		JsonReader jsr = Json.createReader(new StringReader(user));
		JsonObject jso = jsr.readObject();
		jsr.close();
		
		logger.info("Request for Authentication of User : " + jso.getString("userName") + " has been initiated");
		responseBody = jwtService.createOrUpdateUser(jso);
		if(!(boolean)responseBody.get("errors")) {
			logger.info("Authentication Succesfull Response is sent ");
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);
		}else {
			logger.debug("Error occured while Validation of User");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
		}
		
	}
	
	@GetMapping(value = "/user/{tokenId}", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> checkUserValidity(@PathVariable String tokenId){
		Map<String , Object> responseBody = new HashMap<String , Object>();
		
		logger.info("Request to validate a JWT Token has been initiated");
		try {
			Boolean response = jwtService.isValiduser(tokenId);
			responseBody.put("isValid", response);
			logger.info("Validation Response has been sent");
			return ResponseEntity.ok(responseBody);
			
		}catch(Exception e) {
			
			logger.warn("Exception occured while processing token with error message : " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
		
	}
}
