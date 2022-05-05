package com.adminportal.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.domain.User;
import com.adminportal.domain.security.UserRole;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.UserRepository;
import com.adminportal.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if(localUser != null) {
			LOG.info("user {} already exists.", user.getUsername());
			return localUser;
		}
		
		for (UserRole ur : userRoles) {
			if(roleRepository.findByName(ur.getRole().getName()) == null) {
				LOG.info(ur.getRole().getRoleId()+ " " + ur.getRole().getName());
				roleRepository.save(ur.getRole());
			}
		}
		
		user.getUserRoles().addAll(userRoles);
		
		localUser = userRepository.save(user);

		return localUser;
	}
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
