package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MemberLogoutServlet
 */
@WebServlet("/member/logout")
public class MemberLogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberLogoutServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * 로그아웃 -> 세션 파기 -> 쿠키는 건들이지 않는다. (로그아웃 후, 다시 로그인 화면 진입 시 쿠키에 등록된 정보로 아이디 및 체크박스
		 * 처리를 해줌
		 */

		/*
		 * getSession() : true와 동일 getSession(true) : 기존 세션 정보가 존재하면 반환. 없으면 새로운 세션을 반환.
		 * getSession(false) : 기존 세션 정보가 존재하면 반환. 없으면 null을 반환.
		 */
		HttpSession session = request.getSession();

		if (session != null) {
			session.invalidate(); // 세션 객체에 저장된 모든 정보 소멸
		}

		// 4. 결과처리
		response.sendRedirect("/"); // 메인페이지로 이동
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
