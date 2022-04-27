package com.bookstore.service.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.UserShipping;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserShippingService;

@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	public UserShipping findById(Long id) {
		return userShippingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
	}
	
	public void removeById(Long id) {
		userShippingRepository.deleteById(id);
	}
}
