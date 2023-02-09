package net.crudapp.appointment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.crudapp.appointment.model.UserModel;

@Repository
public interface UserRepository  extends JpaRepository<UserModel, Long>{
	UserModel findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
}
