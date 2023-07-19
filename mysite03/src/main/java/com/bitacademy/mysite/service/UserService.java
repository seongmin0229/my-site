package com.bitacademy.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitacademy.mysite.repository.UserRepository;
import com.bitacademy.mysite.vo.UserVo;

@Service
public class UserService {
	
	@Autowired
	private UserRepository ur;

	public void addUser(UserVo vo) {
		ur.insert(vo);
	}

	public UserVo getUser(UserVo vo) {
		return ur.findByEmailAndPassword(vo);
	}

	public UserVo getUser(Long no) {
		return ur.findByNo(no);
	}

	public void updateUser(UserVo vo) {
		ur.update(vo);
	}
}
