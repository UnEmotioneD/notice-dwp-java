package kr.or.iei.member.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.mindrot.jbcrypt.BCrypt;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.member.model.dao.MemberDao;
import kr.or.iei.member.model.vo.Member;

public class MemberService {

	MemberDao dao;

	public MemberService() {
		dao = new MemberDao();
	}

//	public int insertMember(Member member) {
//		Connection conn = JDBCTemplate.getConnection();
//		int result = dao.insertMember(conn, member);
//		if (result > 0) {
//			JDBCTemplate.commit(conn);
//		} else {
//			JDBCTemplate.rollback(conn);
//		}
//		JDBCTemplate.close(conn);
//		return result;
//	}

	// After password encryption
	public int insertMember(Member member) {
		Connection conn = JDBCTemplate.getConnection();

		// 암호화된 비밀번호
		String encPw = BCrypt.hashpw(member.getMemberPw(), BCrypt.gensalt());

		System.out.println("encPw : " + encPw);

		member.setMemberPw(encPw);

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

	// After password encryption
	public Member memberLogin(String loginId, String loginPw) {
		Connection conn = JDBCTemplate.getConnection();
		/*
		 * BCrypt 는 동일한 평문을 암호환 하여도 결과는 항상 다름. 즉 ,로그인 시 SQL 에 입력한 평문을 암호환하여 비교하는 조건식 작성
		 * 불가. BCrypt 에서 평문과 암호환한 데이터가 일치하는지 검사하는 메소드 제공함
		 */
		Member member = dao.memberLogin(conn, loginId, loginPw);
		JDBCTemplate.close(conn);

		if (member == null) {
			return null;
		} else {
			// 평문화 암호화된 데이터가 일치하는가
			boolean login = BCrypt.checkpw(loginPw, member.getMemberPw());

			if (login) {
				return member;
			} else {
				return null;
			}
		}
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

	// TODO MemberDao 에서 함수 명 틀림 원래 서블릿에서도 틀림
	public int updateMemberPw(String memberId, String newMemberPw) {
		Connection conn = JDBCTemplate.getConnection();

		// 새 비밀번호 암호화
		newMemberPw = BCrypt.hashpw(newMemberPw, BCrypt.gensalt());

		int result = dao.updateMemberPw(conn, memberId, newMemberPw);

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

	public String srchInfoId(String memberEmail) {
		Connection conn = JDBCTemplate.getConnection();
		String memberId = dao.srchInfoId(conn, memberEmail);
		JDBCTemplate.close(conn);
		return memberId;
	}

	public String srchInfoPw(String memberId, String memberEmail) {
		Connection conn = JDBCTemplate.getConnection();
		String toEmail = dao.srchInfoPw(conn, memberId, memberEmail);
		JDBCTemplate.close(conn);
		return toEmail;
	}

}
