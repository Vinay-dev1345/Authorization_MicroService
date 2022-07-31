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
	
	@Query(value="SELECT id FROM userdata WHERE user = ?1", nativeQuery = true)
	List<String> checkIsUserExistingOrNot(String name);
	
	@Modifying(clearAutomatically=true)
	@Query(value="UPDATE userdata SET modified_time = ?1 WHERE user = ?2", nativeQuery=true)
	int updateModifyTimeForUser(String dateModified , String name);
}
