package kr.or.iei.member.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.member.model.dao.MemberDao;
import kr.or.iei.member.model.vo.Member;

public class MemberService {

	MemberDao dao;

	public MemberService() {
		dao = new MemberDao();
	}

	public int insertMember(Member member) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.insertMember(conn, member);
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		return result;
	}

	public int idDuplChk(String memberId) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.idDuplChk(conn, memberId);
		JDBCTemplate.close(conn);
		return result;
	}

	public Member memberLogin(String loginId, String loginPw) {
		Connection conn = JDBCTemplate.getConnection();
		Member member = dao.memberLogin(conn, loginId, loginPw);
		JDBCTemplate.close(conn);
		return member;
	}

	public int updateMember(Member updMember) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.updateMemeber(conn, updMember);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public int deleteMember(String memberNo) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.deleteMember(conn, memberNo);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public int updateamemberPw(String memberNo, String newMemberPw) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.deleteMember(conn, memberNo, newMemberPw);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public ArrayList<Member> selectAllMember() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list = dao.selectAllMember(conn);
		JDBCTemplate.close(conn);
		return list;
	}

	public int updChgLevel(String memberNo, String memberLevel) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.updChgLevel(conn, memberNo, memberLevel);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public int updChgAllLevel(String memberNoArrString, String memberLevelArrString) {
		Connection conn = JDBCTemplate.getConnection();

		StringTokenizer st1 = new StringTokenizer(memberNoArrString, "/");
		StringTokenizer st2 = new StringTokenizer(memberLevelArrString, "/");

		boolean resultChk = true;

		while (st1.hasMoreTokens()) {
			String memberNo = st1.nextToken();
			String memberLevel = st2.nextToken();

			int result = dao.updChgLevel(conn, memberNo, memberLevel);

			if (result < 1) {
				resultChk = false;
				break;
			}
		}

		if (resultChk) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		if (resultChk) {
			return 1;
		} else {
			return 0;
		}
	}

}
