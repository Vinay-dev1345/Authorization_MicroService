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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservice.jwtAuthorization.jwtUserEntity.User;
import com.example.microservice.jwtAuthorization.jwtUserRepository.UserRepository;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@Service
public class JwtService {
	
	public static final String SECERET_KEY = "secret";
	public static final int EXPIRATION_TIME_MIN = 30;
	@Autowired
	UserRepository userRepository;
	
	public String createToken(String name) {
		Map<String, Object> hm = new HashMap<String , Object>();
		hm.put("userName", name);
		
		String token = Jwts.builder().setClaims(hm).setExpiration(new Date(System.currentTimeMillis() 
				+ 60 * 1000 * EXPIRATION_TIME_MIN)).signWith(SignatureAlgorithm.HS256, SECERET_KEY).compact();
		return token;
	}
	
	public String isUserValid(String userName, String password) {
		String flag = "inValid";
		List<User> existingUser = userRepository.getCredentialDetails(userName);
		if(existingUser.isEmpty()) {
			flag = "inValid";
		}else{
			for(int i = 0 ; i < existingUser.size() ; i++) {
				User user = existingUser.get(i);
				if(password.equals(user.getPassword())) {
					flag = user.getId();
					System.out.println(flag);
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
				response.put("errors" , true);
			    response.put("errorMsg", "Invalid Credentials");
			}
		}catch(Exception e) {
			response.put("errors" , true);
		    response.put("errorMsg", "Something went wrong !!!!");
		}
		
		return response;
	}
	
	public Boolean isValiduser(String tokenId) {
		String token = tokenId;
		String[] tokenBody = token.split("\\.");
		
//		Base64.Decoder decoder = Base64.getUrlDecoder();
//		String header = new String(decoder.decode(tokenBody[0]));
//		String payload = new String(decoder.decode(tokenBody[1]));
//		System.out.println(payload);
//		System.out.println(header);
		
		Key secretKeySpecification = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECERET_KEY) , SignatureAlgorithm.HS256.getJcaName());
		
		String tokenWithoutSignature = tokenBody[0] + "." + tokenBody[1];
		String signatureObtained = tokenBody[2];
		
		DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, secretKeySpecification);
		Boolean value = validator.isValid(tokenWithoutSignature, signatureObtained);
		
		System.out.println(value);
		return value;
	}
	
}
