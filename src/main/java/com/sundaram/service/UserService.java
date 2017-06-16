package com.sundaram.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sundaram.dao.UserDao;
import com.sundaram.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public User findUserById(String userId) {
		return userDao.findUserById(userId);
	}

	public List<String> getTicks() {
		return userDao.getTicks();
	}
}
