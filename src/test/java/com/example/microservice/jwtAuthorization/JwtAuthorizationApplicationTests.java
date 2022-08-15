package com.example.microservice.jwtAuthorization;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.microservice.jwtAuthorization.jwtService.JwtService;
import com.example.microservice.jwtAuthorization.jwtUserEntity.User;
import com.example.microservice.jwtAuthorization.jwtUserRepository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtAuthorizationApplicationTests {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	JsonObject jso;
	
	@InjectMocks
	private JwtService jwtService;
	
	@Test
	public void checkWhetherTokenCanBeCreatedOrNot() {
		boolean expected = true;
		String token = jwtService.createToken("Vinay");
		boolean actual = !token.isEmpty();
		Assertions.assertEquals(expected,actual);
	}
	
	@Test
	public void checkWhetherTokenIsValid() {
		boolean expected = true;
		String sampleToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6ImphbmUiLCJleHAiOjE2NjA1NjQxMTF9.J0HiZ8Ri88XSuVqNyBgr0hF4zNDpvcesXnQX3ydocws";
		boolean actual = jwtService.isValiduser(sampleToken);
		Assertions.assertEquals(expected,actual);
	}
	
	@Test
	public void checkWhetherTokenIsNotValid() {
		boolean expected = false;
		String sampleToken = "eyJhbGciOiJIUzI1.eyJ1c2VyTmFtZSI6ImphbmUiLCJleHAiOjE2NjA1NjQxMTF9.J0HiZ8Ri88XSuVqNyBgr0hF4zNDpvcesXnQX3ydocws";
		boolean actual = jwtService.isValiduser(sampleToken);
		Assertions.assertEquals(expected,actual);
	}
	
	@Test
	public void userIsAValidUserWhenUserProvidesValidCredentials() {
		List<User> user = Stream.of(new User("1792" , "jackson" , "jackson@123")).collect(Collectors.toList());
		
		String expectedValue = "1792";
		when(userRepository.getCredentialDetails("jackson")).thenReturn(user);
		String actualValue = jwtService.isUserValid("jackson","jackson@123");
		Assertions.assertEquals(expectedValue, actualValue);
	}
	
	@Test
	public void userIsAnInValidUserWhenUserProvidesInValidPassword() {
		List<User> user = Stream.of(new User("1792" , "jackson" , "jackson@123")).collect(Collectors.toList());
		
		String expectedValue = "inValid";
		when(userRepository.getCredentialDetails("jackson")).thenReturn(user);
		String actualValue = jwtService.isUserValid("jackson","jackson@123111");
		Assertions.assertEquals(expectedValue, actualValue);
	}
	
	@Test
	public void userIsAnInValidUserWhenUserProvidesInValidUserName() {
		List<User> user = Stream.of(new User("1792" , "jackson" , "jackson@123")).collect(Collectors.toList());
		
		String expectedValue = "inValid";
		when(userRepository.getCredentialDetails("jack")).thenReturn(new ArrayList<User>());
		String actualValue = jwtService.isUserValid("jack","jackson@123111");
		Assertions.assertEquals(expectedValue, actualValue);
	}
	
	@Test
	public void responseBodyCheckWhenUserIsAValidUser() throws JSONException {
		List<User> user = Stream.of(new User("1792" , "jackson" , "jackson@123")).collect(Collectors.toList());
		
		when(jso.getString("userName")).thenReturn("jackson");
		when(jso.getString("password")).thenReturn("jackson@123");
		when(userRepository.getCredentialDetails("jackson")).thenReturn(user);
		
		Map<String , Object> actualResponse = jwtService.createOrUpdateUser(jso);
		System.out.println(actualResponse);
		Assertions.assertEquals(actualResponse.get("errors"), false);
		
	}
	
	@Test
	public void responseBodyCheckWhenUserIsNotAValidUser() throws JSONException {
		List<User> user = Stream.of(new User("1792" , "jackson" , "jackson@123")).collect(Collectors.toList());
		
		when(jso.getString("userName")).thenReturn("jackson");
		when(jso.getString("password")).thenReturn("jack@123");
		when(userRepository.getCredentialDetails("jackson")).thenReturn(user);
		
		Map<String , Object> actualResponse = jwtService.createOrUpdateUser(jso);
		System.out.println(actualResponse);
		Assertions.assertEquals(actualResponse.get("errors"), true);
		
	}
	

}
