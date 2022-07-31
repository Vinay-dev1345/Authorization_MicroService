package com.example.microservice.jwtAuthorization.jwtUserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.microservice.jwtAuthorization.jwtUserEntity.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
