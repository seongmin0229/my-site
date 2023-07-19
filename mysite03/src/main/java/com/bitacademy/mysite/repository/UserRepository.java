package com.bitacademy.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.bitacademy.mysite.exception.UserRepositoryException;
import com.bitacademy.mysite.vo.UserVo;

@Repository
public class UserRepository {
	
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
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "insert into user values(null, ?, ?, sha2(?, 256), ?, curdate())";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
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
	
	public UserVo findByEmailAndPassword(UserVo vo) {
		return findByEmailAndPassword(vo.getEmail(), vo.getPassword());
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo authUser = null;
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "select no, name from user where email=? and password = sha2(?, 256)";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setString(1, email);
			pstmt.setString(2, password);
//			pstmt.setString(3, vo.getPassword());
//			pstmt.setString(4, vo.getGender());
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			
			if (rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				
				authUser = new UserVo();
				authUser.setNo(no);
				authUser.setName(name);
			}
		} catch (SQLException e) {
			throw new UserRepositoryException(e.toString());
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
		return authUser;
	}
	
	public UserVo findByNo(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo result = null;
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "select no, name, email, gender from user where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setLong(1, no);
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			if(rs.next()) {
				result = new UserVo();
				result.setNo(rs.getLong(1));
				result.setName(rs.getString(2));
				result.setEmail(rs.getString(3));
				result.setGender(rs.getString(4));
			}

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
		return result;
	
	}

	public boolean update(UserVo updateVo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;
		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "update user set name = ?, email = ?, password = sha2(?, 256), gender = ? where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setString(1, updateVo.getName());
			pstmt.setString(2, updateVo.getEmail());
			pstmt.setString(3, updateVo.getPassword());
			pstmt.setString(4, updateVo.getGender());
			pstmt.setLong(5, updateVo.getNo());
			//5. SQL 실행
			int count = pstmt.executeUpdate();
			
			//5. 결과 처리
			result = count == 1;

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
		return result;
	
	}
}
