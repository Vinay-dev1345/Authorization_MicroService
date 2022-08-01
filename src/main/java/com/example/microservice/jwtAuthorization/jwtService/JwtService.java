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
	
	@Autowired
	UserRepository userRepository;
	
	public String createToken(String name) {
		Map<String, Object> hm = new HashMap<String , Object>();
		hm.put("userName", name);
		
		String token = Jwts.builder().setClaims(hm).signWith(SignatureAlgorithm.HS256, SECERET_KEY).compact();
		System.out.println(token);
		return token;
	}
	
	public Boolean isExistingUser(String userName) {
		List<String> existingUser = userRepository.checkIsUserExistingOrNot(userName);
		if(existingUser.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	public Map<String, Object> createOrUpdateUser(JsonObject jso) {
		Map<String, Object> response = new HashMap<String , Object>();
		if(!isExistingUser(jso.getString("userName"))) {
			try {
				User user =new User();
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Random ran = new Random();
				
				String id = Long.toString(ran.nextLong(10000000 , 99999999));
				String name = jso.getString("userName");
				String currentDateTime = sdf.format(date);
				
				user.setUser(name);
				user.setCreatedTime(currentDateTime);
				user.setModifiedTime(currentDateTime);
				user.setId(id);
				
				userRepository.save(user);
				String generatedToken = createToken(name);
				
				response.put("errors", false);
				response.put("accessToken", generatedToken);
				
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}
		else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				
				String currentDateTime = sdf.format(date);
				String userName = jso.getString("userName");
				
				int dbResult = userRepository.updateModifyTimeForUser(currentDateTime, userName);
				if(dbResult > 0) {
					String generatedToken = createToken(userName);
					response.put("errors", false);
					response.put("accessToken", generatedToken);
				}
				else {
					response.put("errors", true);
				}
				
			}catch(Exception e){
				System.out.println(e.toString());
			}

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
