package com.example.useraccessdivide.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.useraccessdivide.user.entities.AuthenticationsEntity;

@Repository
public interface AuthenticationRepository extends JpaRepository<AuthenticationsEntity, Integer> {
	@Query(value = "SELECT authen.* FROM authentications authen WHERE "
			+ "authen.history_id = (SELECT max(a.history_id) FROM authentications a WHERE a.user_id = authen.user_id) "
			+ "AND authen.user_id = ?1",
			nativeQuery = true)
	AuthenticationsEntity findByUzer(long userId);
}
