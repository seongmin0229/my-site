package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bitacademy.mysite.vo.GuestBookVo;
	
public class GuestBookDao {
	
	public Connection getConnection() throws SQLException {
		try {
			//1. JDBC Driver Class 로딩
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		//2. 연결하기
		String url = "jdbc:mysql://localhost:3306/webdb?charset=utf8";
		return DriverManager.getConnection(url, "root", "root");
	}
	
	public List<GuestBookVo> findAll(){
		List<GuestBookVo> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "select no, name, text, post_date from guestbook order by no asc";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String text = rs.getString(3);
				String postDate = rs.getString(4);
				
				GuestBookVo vo = new GuestBookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setText(text);
				vo.setPostDate(postDate);
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		} finally {
			//6. 자원 정리
			try {
				if(rs != null) {
					rs.close();
				}
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
		
		return result;
	}
	
	public boolean insert(GuestBookVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "insert into guestbook values(null, ?, sha2(?, 256), curdate(), ?)";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getText());
			//5. SQL 실행
			int count = pstmt.executeUpdate();
			
			//5. 결과 처리
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
			result = count == 1;
			rearrange();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		} finally {
			//6. 자원 정리
			try {
				if(rs != null) {
					rs.close();
				}
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
		return result;
	}
	
	
	public boolean delete(GuestBookVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "delete from guestbook where no = ? and password = sha2(?, 256)";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setLong(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());
			//5. SQL 실행
			int count = pstmt.executeUpdate();
			
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
			result = count == 1;
			rearrange();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		} finally {
			//6. 자원 정리
			try {
				if(rs != null) {
					rs.close();
				}
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
		return result;
	}
	
	public void rearrange() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			
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
