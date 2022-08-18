package com.example.microservice.jwtAuthorization.jwtService;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;
import javax.json.JsonObject;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservice.jwtAuthorization.JwtAuthorizationApplication;
import com.example.microservice.jwtAuthorization.jwtUserEntity.User;
import com.example.microservice.jwtAuthorization.jwtUserRepository.UserRepository;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@Service
public class JwtService {
	private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationApplication.class);
	public static final String SECERET_KEY = "4A9TYU6J9OP1BN578LIQ29JN03BD2LZ1";
	public static final int EXPIRATION_TIME_MIN = 5;
	@Autowired
	UserRepository userRepository;
	
	public String createToken(String name) {
		Map<String, Object> hm = new HashMap<String , Object>();
		hm.put("userName", name);
		
		String token = Jwts.builder().setClaims(hm).setExpiration(new Date(System.currentTimeMillis() 
				+ 60 * 1000 * EXPIRATION_TIME_MIN)).signWith(SignatureAlgorithm.HS256, SECERET_KEY).compact();
		logger.info("Created JWT Token Successfully");
		return token;
	}
	
	public String isUserValid(String userName, String password) {
		String flag = "inValid";
		List<User> existingUser = null;
		try {
			existingUser = userRepository.getCredentialDetails(userName);
		}catch(Exception e) {
			logger.debug(" Data Extraction from User DataBase Failed ");;
		}
		
		if(existingUser.isEmpty()) {
			logger.warn("User Not Found");
			flag = "inValid";
		}else{
			for(int i = 0 ; i < existingUser.size() ; i++) {
				User user = existingUser.get(i);
				if(password.equals(user.getPassword())) {
					flag = user.getId();
					logger.info("User : "+ userName + " Authenticated Sucessfully !!");
					break;
				}
			}
		}
		return flag;
		
	}
	
	public Map<String, Object> createOrUpdateUser(JsonObject jso) {
		Map<String, Object> response = new HashMap<String , Object>();
		String user = jso.getString("userName");
		String password = jso.getString("password");
		
		try {
			String userId = isUserValid(user , password);
			if(!userId.equals("inValid")) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			    Date date = new Date();
			    String currentDateTime = sdf.format(date);
			    
			    userRepository.updateModifyTimeForUser(currentDateTime , userId);
			    
			    String token = createToken(user);
			    
			    response.put("accessToken", token);
			    response.put("errors" , false);
			    response.put("errorMsg", "");
			    
			}else {
				logger.warn("User " + user + " has provided Invalid Credentials ");
				response.put("errors" , true);
			    response.put("errorMsg", "Invalid Credentials");
			}
		}catch(Exception e) {
			logger.debug("UnExpected exception occured ");
			System.out.println(e.toString());
			response.put("errors" , true);
		    response.put("errorMsg", "Something went wrong !!!!");
		}
		
		return response;
	}
	
	public Boolean isValiduser(String tokenId) {
		boolean value = false;
		try {
			String token = tokenId;
			String[] tokenBody = token.split("\\.");
			
			Key secretKeySpecification = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECERET_KEY) , SignatureAlgorithm.HS256.getJcaName());
			
			String tokenWithoutSignature = tokenBody[0] + "." + tokenBody[1];
			String signatureObtained = tokenBody[2];
			
			DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, secretKeySpecification);
			value = validator.isValid(tokenWithoutSignature, signatureObtained);
			
			if(value) {
				logger.info("Token is valid ");
			}else {
				logger.warn("Token is Invalid");
			}
		}catch(Exception e) {
			logger.debug("Unexpected execption occured while processing token");
		}

		return value;
	}
	
}
