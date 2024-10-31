package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.member.model.vo.Member;

/**
 * Servlet implementation class MemberUpdateServlet
 */
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberUpdateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 인코딩 ->필터

		// 값 추출
		String memberNo = request.getParameter("memberNo");// 수정시 where 조건식에 필요한 데이터
		String memberName = request.getParameter("memberName");
		String memberEmail = request.getParameter("memberEmail");
		String memberPhone = request.getParameter("memberPhone");
		String memberAddr = request.getParameter("memberAddr");

		// 로직 - 정보수정
		Member updMember = new Member();
		updMember.setMemberNo(memberNo);
		updMember.setMemberName(memberName);
		updMember.setMemberEmail(memberEmail);
		updMember.setMemberPhone(memberPhone);
		updMember.setMemberAddr(memberAddr);

		MemberService service = new MemberService();
		int result = service.updateMember(updMember);

		// 결과처리

		if (result > 0) {
			// 정상 정보 수정 시 다시 로그인 하도록
			// 로그인이 해제된다 == 세션에 등록된 정보를 소멸시킨다
			// 세션에 등록된 정보를 소멸시키는 코드는 로그아웃 서블릿에 존재
			// 로그아웃 서블릿으로 sendRedirect를 하면 상단 url 에 주소값이 변경됨
			// 현재 회원 정보 수정 서블릿에서 세션을 파기

			// 1) 재로그인을 위해 새션을 파기하고 로그인 화면으로 이동
			/*
			 * HttpSession session = request.getSession(false);
			 * 
			 * if (session != null) { session.invalidate(); }
			 * 
			 * request.setAttribute("title", "알림"); request.setAttribute("msg",
			 * "회원 정보가 수정되었습니다 재로그인 하시기바랍니다"); request.setAttribute("icon", "success");
			 * request.setAttribute("loc", "/member/loginFrm");
			 */

			// 2) 정상적으로 수정되었을때 , 마이페이지로 이동
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "회원 정보가 수정되었습니다 재로그인 하시기바랍니다");
			request.setAttribute("icon", "success");
			request.setAttribute("loc", "/member/loginFrm");

			// 2-1) 아무런 작업없이, mypage.jsp 로 이동하느경우 세션에는 정보가 없데이트 되지 않았기 때문에 수정 이전의 데이터가 표기된다

			/* 기존 로그인 메소드 재사용 */
			// mypage.jsp 에서 id 와 pw 을 전송해 주거나 현재 서블릿에서 세션에 존재하는 회원 정보중 id 와 pw 를 get 하거나
//      service.memberLogin(updMember.getMemberId(), updMember.getMemberPw());
//			HttpSession session = request.getSession(false);
//			session.setAttribute("loginMember", m);

			// 세션에 있는 정보를 바꿔준다
			HttpSession session = request.getSession(false);
			Member sessionMember = (Member) session.getAttribute("loginMember");

			sessionMember.setMemberName(memberName);
			sessionMember.setMemberPhone(memberPhone);
			sessionMember.setMemberEmail(memberEmail);
			sessionMember.setMemberAddr(memberAddr);

		} else {
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "회원 정보 수정 중 오류가 발생했습니다");
			request.setAttribute("icon", "error");
			request.setAttribute("loc", "/member/mypage");
		}

		request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
