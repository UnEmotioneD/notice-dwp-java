package kr.or.iei.member.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.member.model.vo.Member;

public class MemberDao {

	public int insertMember(Connection conn, Member member) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "INSERT INTO TBL_MEMBER VALUES (TO_CHAR(SYSDATE, 'yymmdd') || LPAD(SEQ_MEMBER.NEXTVAL, 4, '0'), ?, ?, ?, ?, ?, ?, 3, SYSDATE)";
		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, member.getMemberId());
			pt.setString(2, member.getMemberPw());
			pt.setString(3, member.getMemberName());
			pt.setString(4, member.getMemberEmail());
			pt.setString(5, member.getMemberPhone());
			pt.setString(6, member.getMemberAddr());
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int idDuplChk(Connection conn, String memberId) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		String query = "SELECT COUNT(*) AS CNT FROM TBL_MEMBER WHERE MEMBER_ID = ?";
		int cnt = 0;

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, memberId);
			rt = pt.executeQuery();

			if (rt.next()) {
				// select query 결과를 Integer 로 꺼내온다
				cnt = rt.getInt("CNT");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return cnt;
	}

//	public Member memberLogin(Connection conn, String loginId, String loginPw) {
//		PreparedStatement pt = null;
//		ResultSet rt = null;
//		Member m = null;
//		String query = "SELECT * FROM TBL_MEMBER WHERE MEMBER_ID = ? AND MEMBER_PW = ?";
//
//		try {
//			pt = conn.prepareStatement(query);
//			pt.setString(1, loginId);
//			pt.setString(2, loginPw);
//			rt = pt.executeQuery();
//
//			if (rt.next()) {
//				m = new Member();
//				m.setMemberNo(rt.getString("MEMBER_NO"));
//				m.setMemberId(rt.getString("MEMBER_ID"));
//				m.setMemberPw(rt.getString("MEMBER_PW"));
//				m.setMemberName(rt.getString("MEMBER_NAME"));
//				m.setMemberEmail(rt.getString("MEMBER_EMAIL"));
//				m.setMemberPhone(rt.getString("MEMBER_PHONE"));
//				m.setMemberAddr(rt.getString("MEMBER_ADDR"));
//				m.setMemberLevel(rt.getInt("MEMBER_LEVEL"));
//				m.setEnrollDate(rt.getString("ENROLL_DATE"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			JDBCTemplate.close(rt);
//			JDBCTemplate.close(pt);
//		}
//		return m;
//	}

	// After password encryption
	public Member memberLogin(Connection conn, String loginId, String loginPw) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		Member m = null;
		String query = "SELECT * FROM TBL_MEMBER WHERE MEMBER_ID = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, loginId);
			rt = pt.executeQuery();

			if (rt.next()) {
				m = new Member();
				m.setMemberNo(rt.getString("MEMBER_NO"));
				m.setMemberId(rt.getString("MEMBER_ID"));
				m.setMemberPw(rt.getString("MEMBER_PW"));
				m.setMemberName(rt.getString("MEMBER_NAME"));
				m.setMemberEmail(rt.getString("MEMBER_EMAIL"));
				m.setMemberPhone(rt.getString("MEMBER_PHONE"));
				m.setMemberAddr(rt.getString("MEMBER_ADDR"));
				m.setMemberLevel(rt.getInt("MEMBER_LEVEL"));
				m.setEnrollDate(rt.getString("ENROLL_DATE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return m;
	}

	public int updateMemeber(Connection conn, Member updMember) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "UPDATE TBL_MEMBER SET MEMBER_NAME = ?, MEMBER_EMAIL = ?, MEMBER_PHONE = ?, MEMBER_ADDR = ? WHERE MEMBER_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, updMember.getMemberName());
			pt.setString(2, updMember.getMemberEmail());
			pt.setString(3, updMember.getMemberPhone());
			pt.setString(4, updMember.getMemberAddr());
			pt.setString(5, updMember.getMemberNo());

			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int deleteMember(Connection conn, String memberNo) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "DELETE FROM TBL_MEMBER WHERE MEMBER_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, memberNo);

			result = pt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int deleteMember(Connection conn, String memberNo, String newMemberPw) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "UPDATE TBL_MEMBER SET MEMBER_PW = ? WHERE MEMBER_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, newMemberPw);
			pt.setString(2, memberNo);

			result = pt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	// after password encryption
	public int updateMemberPw(Connection conn, String memberId, String newMemberPw) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "update tbl_member set member_pw = ? where member_id = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, newMemberPw);
			pt.setString(2, memberId);

			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public ArrayList<Member> selectAllMember(Connection conn) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		ArrayList<Member> list = new ArrayList<>();
		String query = "SELECT * FROM TBL_MEMBER WHERE MEMBER_LEVEL != '1' ORDER BY MEMBER_NO";

		try {
			pt = conn.prepareStatement(query);
			rt = pt.executeQuery();

			while (rt.next()) {
				Member m = new Member();
				m.setMemberNo(rt.getString("member_no"));
				m.setMemberId(rt.getString("member_id"));
				m.setMemberName(rt.getString("member_name"));
				m.setMemberPhone(rt.getString("member_phone"));
				m.setMemberEmail(rt.getString("member_email"));
				m.setMemberAddr(rt.getString("member_addr"));
				m.setMemberLevel(rt.getInt("member_level"));
				m.setEnrollDate(rt.getString("enroll_date"));
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return list;
	}

	public int updChgLevel(Connection conn, String memberNo, String memberLevel) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "UPDATE TBL_MEMBER SET MEMBER_LEVEL = ? WHERE MEMBER_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, memberLevel);
			pt.setString(2, memberNo);
			result = pt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public String srchInfoId(Connection conn, String memberEmail) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		String query = "SELECT MEMBER_ID FROM TBL_MEMBER WHERE MEMBER_EMAIL = ?";
		String memberId = null;

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, memberEmail);
			rt = pt.executeQuery();

			if (rt.next()) {
				memberId = rt.getString("member_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return memberId;
	}

	public String srchInfoPw(Connection conn, String memberId, String memberEmail) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		String query = "SELECT MEMBER_PW FROM TBL_MEMBER WHERE MEMBER_ID = ? AND MEMBER_EMAIL = ?";
		String toEmail = "";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, memberId);
			pt.setString(2, memberEmail);

			rt = pt.executeQuery();

			if (rt.next()) {
				toEmail = rt.getString("MEMBER_PW");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return toEmail;
	}

}
