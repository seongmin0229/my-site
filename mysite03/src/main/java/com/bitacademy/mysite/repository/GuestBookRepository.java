package com.bitacademy.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bitacademy.mysite.vo.GuestBookVo;
	
@Repository
public class GuestBookRepository {
	
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private SqlSession sqlSession;
	
	
	public List<GuestBookVo> findAll(){
		return sqlSession.selectList("guestbook.findAll");
		
	}
	
	public boolean insert(GuestBookVo vo) {
		return 1 == sqlSession.insert("guestbook.insert", vo);
	}
	
	
	public boolean delete(Long no, String password) {
		boolean result = false;
		
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("password", password);
		
		sqlSession.delete("guestbook.delete", map);
		
		return result;
	}
	
	public void rearrange() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			
			//3. Statement 준비
			String sqls[] = {"SET @cnt = 0",
			"UPDATE guestbook SET no = @cnt:= @cnt + 1", 
			"ALTER TABLE guestbook AUTO_INCREMENT = 1"};
			for(String sql : sqls) {
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}

			
			//5. 결과 처리
//			String password = rs.getString(1);
//			while(rs.next()) {
//				Long no = rs.getLong(1);
//				String firstName = rs.getString(2);
//				String lastName = rs.getString(3);
//				String email = rs.getString(4);
//				
//				EmaillistVo vo = new EmaillistVo();
//				vo.setNo(no);
//				vo.setFirstName(firstName);
//				vo.setLastName(lastName);
//				vo.setEmail(email);
//			}
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		} finally {
			//6. 자원 정리
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
