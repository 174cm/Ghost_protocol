package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

import client.swing.T_board;
/**
 * 
 * @author 고명주
 * @brief 데이터베이스에 접근하는 클래스, 데이터 베이스를 처리해줌
 */
public class BoardDAO {
	// 데이터 베이스에 접근하는 객체
	private static final Logger logger = Logger.getLogger(BoardDAO.class.getName());
	// 여기에서 데이터 베이스 처리
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/board?serverTimezone=UTC";
	// URL은 mysql 경로.
	private static final String USER = "root"; // DB ID
	private static final String PASSWORD = "park94"; // DB Password
	T_board tboard;

	public BoardDAO() { // 디폴트 생성자가 없으면 실행이 안된다.

	}

	public BoardDAO(T_board tboard) { // 생성자 중복정의
		this.tboard = tboard;
		System.out.println("DAO =>" + tboard);
	}

	// DB연결 메소드 정의

	public Connection getConn() {
		Connection con = null;

		try {
			Class.forName(DRIVER).newInstance(); // 드라이버 로딩
			System.out.println("[DB loading...]");
			con = DriverManager.getConnection(URL, USER, PASSWORD); // 드라이버에 연결
			System.out.println("[DB connect]");
		} catch (Exception e) {
			logger.warning("[DB failed]");
		}

		return con;
	}

	// 작성 창에서 정보를 얻어옴
	@SuppressWarnings("finally")
	public BoardDTO getBoardDTO(String name) {
		BoardDTO dto = new BoardDTO();

		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과값

		try {

			
			con = getConn();
			String sql = "select * from boardtable where name = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, name);

			rs = ps.executeQuery();

			if (rs.next()) {
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("Pwd"));
				dto.setTitle(rs.getString("title"));
				dto.setContents(rs.getString("contents"));
				dto.setCategorize(rs.getString("categorize"));

			}
			
		} catch (Exception e) {
			logger.warning("[name을 찾지 못함.]");
		}
		finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (final Exception e) {  }
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (final Exception e) {  }
		    }
		    if (con != null) {
		        try {
		            con.close();
		        } catch (final Exception e) {  }
		    }
		    
		}
		return dto;
	}

	// 테이블 출력
	public Vector getcontents_List() {

		Vector data = new Vector(); // JTable에 값을 넣기 위하여 벡터를 이용하였다.

		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과

		try {

			con = getConn();
			String sql = "select * from boardtable order by name asc";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String title = rs.getString("title");
				String categorize = rs.getString("categorize");

				Vector row = new Vector();
				row.add(name);
				row.add(title);
				row.add(categorize);

				data.add(row);
			}
		} catch (Exception e) {
			logger.warning(" ");
		}
		finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (final Exception e) {  }
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (final Exception e) {  }
		    }
		    if (con != null) {
		        try {
		            con.close();
		        } catch (final Exception e) {  }
		    }
		    
		}
		return data;
	}

	// 글 등록
	public boolean insertboard(BoardDTO dto) {

		boolean ok = false;

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = getConn();
			String sql = "insert into boardtable(" + "name, pwd, title, contents, categorize)" + "values(?, ? ,? ,?,?)";

			ps = con.prepareStatement(sql);

			ps.setString(1, dto.getName());
			ps.setString(2, dto.getPwd());
			ps.setString(3, dto.getTitle());
			ps.setString(4, dto.getContents());
			ps.setString(5, dto.getCategorize());

			int r = ps.executeUpdate(); // 실행 결과를 저장

			if (r > 0) {
				System.out.println("등록 성공");
				ok = true;
			} else {
				System.out.println("등록 실패");
			}

		} catch (Exception e) {
			logger.warning("등록부분 error");
		}
		finally {

		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (final Exception e) {  }
		    }
		    if (con != null) {
		        try {
		            con.close();
		        } catch (final Exception e) {  }
		    }
		    
		}
		return ok;
	}

	// 게시물 수정
	public boolean updateboard(BoardDTO vCon) {
		System.out.println("dto =" + vCon.toString());
		boolean ok = false;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConn();
			String sql = "update boardtable set title = ?, contents = ?, categorize = ?" + "where name = ? and pwd = ?";

			ps = con.prepareStatement(sql);

			ps.setString(4, vCon.getName()); // 앞의 순서는 sql 명령어 ? 안에 들어갈 순서
			ps.setString(5, vCon.getPwd());
			ps.setString(1, vCon.getTitle());
			ps.setString(2, vCon.getContents());
			ps.setString(3, vCon.getCategorize());

			int r = ps.executeUpdate(); // 실행 -> 수정 (0일 때 실패)

			if (r > 0) {
				ok = true;
			}
		} catch (Exception e) {
			logger.warning("[게시물 수정 error]");
		}
		finally {
		   
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (final Exception e) {  }
		    }
		    if (con != null) {
		        try {
		            con.close();
		        } catch (final Exception e) {  }
		    }
		    
		
		return ok;
	}

	// 게시글 삭제 메소드
	public boolean deleteboard(String name, String pwd) {

		boolean ok = false;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = getConn();
			String sql = "delete from boardtable where name = ? and pwd =?";

			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, pwd);
			int r = ps.executeUpdate();

			if (r > 0)
				ok = true;

		} catch (Exception e) {
			System.out.println(e + "-> 오류발생");
		}
		finally {
		   
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (final Exception e) {  }
		    }
		    if (con != null) {
		        try {
		            con.close();
		        } catch (final Exception e) {  }
		    }
		    
		}
		return ok;
	}

	// 데이터 베이스 내의 정보 다시 불러오는 메소드
	public void userSelectAll(DefaultTableModel model) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConn();
			String sql = "select * from boardtable order by name asc";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			for (int i = 0; i < model.getRowCount();) {
				model.removeRow(0);
			}
			while (rs.next()) {
				Object data[] = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) };
				model.addRow(data);
			}

		} catch (SQLException e) {
			System.out.println(e + "=> userSelectAll fail");
		} finally {

			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.info("rs not close");
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.info("ps not close");
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.info("con not close");
				}
		}
	}
}
