package net.crudapp.appointment.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.crudapp.appointment.model.UserModel;
import net.crudapp.appointment.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	UserRepository userRepository;
	
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    UserModel user = userRepository.findByUsername(username);

	        return UserDetailsImpl.build(user);
	      }
}
