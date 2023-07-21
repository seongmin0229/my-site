package com.bitacademy.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bitacademy.mysite.vo.GuestBookVo;
	
@Repository
public class GuestBookRepository {
	@Autowired
	private SqlSession sqlSession;
	
	
	public List<GuestBookVo> findAll(){
		return sqlSession.selectList("guestbook.findAll");
	}
	
	public boolean insert(GuestBookVo vo) {
		boolean result = false;
		
		result = 1 == sqlSession.insert("guestbook.insert", vo);
		
		if(result) {
//			rearrange();
		}
		
		return result;
	}
	
	
	public boolean delete(Long no, String password) {
		boolean result = false;
		
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("password", password);
		
		result = 1 == sqlSession.delete("guestbook.delete", map);
		
		if(result) {
//			rearrange();
		}
		
		return result;
	}
	
	public void rearrange() {
		sqlSession.update("guestbook.rearrange");
	}
}
