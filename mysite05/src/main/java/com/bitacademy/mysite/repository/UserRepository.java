package com.bitacademy.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bitacademy.mysite.vo.UserVo;

@Repository
public class UserRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	public Connection getConnection() throws SQLException {
		//1. JDBC Driver Class 로딩
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2. 연결하기
		String url = "jdbc:mysql://localhost:3306/webdb?charset=utf8";
		return DriverManager.getConnection(url, "root", "root");
	}
	
	public boolean insert(UserVo vo) {
		return 1 == sqlSession.insert("user.insert", vo);
	}
	
	public UserVo findByEmailAndPassword(UserVo vo) {
		return findByEmailAndPassword(vo.getEmail(), vo.getPassword());
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		Map<String, Object> map = new HashMap<>();
		map.put("e", email);
		map.put("p", password);
		
		return sqlSession.selectOne("user.findByEmailAndPassword", map);
	}
	
	public UserVo findByNo(Long no) {
		return sqlSession.selectOne("user.findByNo", no);
	}

	public boolean update(UserVo vo) {
		return 1 == sqlSession.update("user.update", vo);
	}
}
