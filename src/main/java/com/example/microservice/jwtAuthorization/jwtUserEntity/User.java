package com.example.microservice.jwtAuthorization.jwtUserEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userdata")
public class User {
	@Id
	private String id;
	private String username;
	private String password;
	private String lastUpdate;

	public User() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public User(String id, String username, String password) {
		this.id = id;
		this.password = password;
		this.username = username;
	}

}
