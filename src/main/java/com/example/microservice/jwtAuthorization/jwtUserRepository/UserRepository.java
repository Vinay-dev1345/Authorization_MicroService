package com.example.microservice.jwtAuthorization.jwtUserRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.microservice.jwtAuthorization.jwtUserEntity.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	@Query(value="SELECT * FROM userdata WHERE username = ?1", nativeQuery = true)
	List<User> getCredentialDetails(String name);
	
	@Modifying(clearAutomatically=true)
	@Query(value="UPDATE userdata SET last_update = ?1 WHERE id = ?2", nativeQuery=true)
	int updateModifyTimeForUser(String dateModified , String userId);
}
