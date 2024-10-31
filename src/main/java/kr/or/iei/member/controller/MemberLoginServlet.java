package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.member.model.vo.Member;

/**
 * Servlet implementation class MemberLoginServlet
 */
@WebServlet("/member/login")
public class MemberLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberLoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 인코딩 -> 필터
		// 2. 값 추출
		String loginId = request.getParameter("loginId");
		String loginPw = request.getParameter("loginPw");
		// 3. 로직 -> 로그인
		MemberService service = new MemberService();
		Member loginMember = service.memberLogin(loginId, loginPw);
		// 4. 결과 처리

		if (loginMember != null) {
			// 로그인 정보는 존재하지만, level 이 3인 경우
			if (loginMember.getMemberLevel() == 3) {
				request.setAttribute("title", "알림");
				request.setAttribute("msg", "로그인 권한이 없습니다. 관리자에게 문의하십시오");
				request.setAttribute("icon", "error");
				request.setAttribute("loc", "/member/loginFrm");

				request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
				return;
			}

			/*
			 * getSession(true) : 기존에 세션이 존재하면, 해당 세션을 반환. 존재하지 않으면 세션을 생성하여 변환
			 * getSession(false) : 기존에 세션이 존재하면, 해당 세션을 반환. 존재하지 않으면 null 을 반환
			 */
			HttpSession session = request.getSession(true);
			session.setAttribute("loginMember", loginMember);
			session.setMaxInactiveInterval(600); // 초 단위

			/*
			 * 쿠키 : 클라이언트에 저장되는 공간을 의미 - 세션에 비해 상대적으로 보안이 취약하다 -
			 */
			Cookie cookie = new Cookie("saveId", loginId);

			if (request.getParameter("saveId") != null) {
				// 아이디 저장 체크박스를 체크한 경우
				cookie.setMaxAge(60 * 60 * 24 * 30); // 초단위 60 * 60 * 24 * 30은 30일이다
			} else {
				// 아이디 저장 체크박스를 체크하지 않은 경우
				cookie.setMaxAge(0); // 유효시간 초기화 = 쿠키 해제
			}

			// 쿠키를 적용시킬 경로
			cookie.setPath("/member/loginFrm");

			// 쿠키는 클라이언트에 저장되는 정보이므로 응답 객체를 통해 전달
			response.addCookie(cookie);

			response.sendRedirect("/");

		} else {
			// 로그인 실패시
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			request.setAttribute("icon", "error");
			// 다시 로그인 페이지로
			request.setAttribute("loc", "/member/loginFrm");

			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp");
			view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
