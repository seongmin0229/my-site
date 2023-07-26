package com.bitacademy.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitacademy.mysite.repository.GuestBookRepository;
import com.bitacademy.mysite.vo.GuestBookVo;

@Service
public class GuestBookService {
	
	@Autowired
	private GuestBookRepository gbr;
	
	public List<GuestBookVo> getMessageList(){
		return gbr.findAll();
	}
	
	public void deleteMessage(Long no, String password) {
		gbr.delete(no, password);
	}
	
	public void addMessage(GuestBookVo vo) {
		gbr.insert(vo);
	}

	public void deleteMessage(GuestBookVo vo) {
		deleteMessage(vo.getNo(), vo.getPassword());
	}
}
