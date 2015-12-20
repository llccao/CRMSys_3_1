package com.meng.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meng.crm.dao.UserMapper;
import com.meng.crm.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserMapper mapper;	
	
	@Transactional(readOnly=true)
	public User getByName(String name,String password) {
		
		User user = mapper.getByName(name);
		
		if(user != null && user.getEnabled() == 1 && user.getPassword().equals(password)) {
			
			return user;
		}
		return null;
	}
}
