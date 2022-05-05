package com.bookstore.service.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.security.Role;
import com.bookstore.repository.RoleRepository;
import com.bookstore.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Role findById(int id) {
		return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
}
