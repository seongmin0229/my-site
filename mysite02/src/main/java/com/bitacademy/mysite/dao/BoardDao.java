package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bitacademy.mysite.vo.BoardVo;

public class BoardDao {
	
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
	
	public boolean insert(BoardVo parentVo, BoardVo childVo) {
		System.out.println("insert accur");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = getConnection();
			//3. Statement 준비
			String sql = "insert into board "
					+ "select null, ?, ?, ?, curdate(), ?, ?, ?, ? ";
			String sql2 = "insert into board "
					+ "select null, ?, ?, ?, curdate(), ifnull(max(g_no), 0) + 1, ?, ?, ? "
					+ "from board";
			if(parentVo != null) {
				pstmt = conn.prepareStatement(sql);
				//4. Binding
				pstmt.setString(1, childVo.getTitle());
				pstmt.setString(2, childVo.getContent());
				pstmt.setLong(3, 0);
				pstmt.setLong(4, parentVo.getGno());
				updateGroupSet(parentVo.getGno(), parentVo.getOno() + 1);
				pstmt.setLong(5, parentVo.getOno() + 1);
				pstmt.setLong(6, parentVo.getDepth() + 1);
				pstmt.setLong(7, childVo.getUserNo());
			}else {
				pstmt = conn.prepareStatement(sql2);
				//4. Binding
				pstmt.setString(1, childVo.getTitle());
				pstmt.setString(2, childVo.getContent());
				pstmt.setLong(3, 0);
				pstmt.setLong(4, 1);
				pstmt.setLong(5, 0);
				pstmt.setLong(6, childVo.getUserNo());
			}
			

			
			//5. SQL 실행
			int count = pstmt.executeUpdate();
			
			//5. 결과 처리
			result = count == 1;
		}catch (SQLException e) {
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
	
	public boolean updateGroupSet(Long gno, Long ono) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;
		try {
			conn = getConnection();
			//3. Statement 준비
			String sql = "update board set o_no = o_no + 1 where g_no = ? and o_no >= ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setLong(1, gno);
			pstmt.setLong(2, ono);
			
			//5. SQL 실행
			int count = pstmt.executeUpdate();
			
			//5. 결과 처리
			result = count == 1;
		}catch (SQLException e) {
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
	
	public List<BoardVo> findAll(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> result = new ArrayList<>();
		try {
			conn = getConnection();
			//3. Statement 준비
			String sql = "select * from board order by g_no desc, o_no asc";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			while(rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setContent(rs.getString(3));
				vo.setHit(rs.getLong(4));
				vo.setRegDate(rs.getString(5));
				vo.setGno(rs.getLong(6));
				vo.setOno(rs.getLong(7));
				vo.setDepth(rs.getLong(8));
				vo.setUserNo(rs.getLong(9));
				result.add(vo);
			}
		}catch (SQLException e) {
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

	public BoardVo findByNo(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo vo = null;
		try {
			conn = getConnection();
			//3. Statement 준비
			String sql = "select * from board where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setLong(1, no);
			
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			if(rs.next()) {
				vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setContent(rs.getString(3));
				vo.setHit(rs.getLong(4));
				vo.setRegDate(rs.getString(5));
				vo.setGno(rs.getLong(6));
				vo.setOno(rs.getLong(7));
				vo.setDepth(rs.getLong(8));
				vo.setUserNo(rs.getLong(9));
			}
		}catch (SQLException e) {
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
		return vo;
	}

	public int length() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = getConnection();
			//3. Statement 준비
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			if(rs.next()) {
				result = rs.getInt(1);
			}
		}catch (SQLException e) {
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

	public List<BoardVo> findAllByRange(int selected) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> result = new ArrayList<>();
		try {
			conn = getConnection();
			//3. Statement 준비
			String sql = "select * from board order by g_no desc, o_no asc limit ?, 5";
			pstmt = conn.prepareStatement(sql);
			
			//4. Binding
			pstmt.setInt(1, (selected - 1) * 5);
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//5. 결과 처리
			while(rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setContent(rs.getString(3));
				vo.setHit(rs.getLong(4));
				vo.setRegDate(rs.getString(5));
				vo.setGno(rs.getLong(6));
				vo.setOno(rs.getLong(7));
				vo.setDepth(rs.getLong(8));
				vo.setUserNo(rs.getLong(9));
				result.add(vo);
			}
		}catch (SQLException e) {
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
