package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.member.model.service.MemberService;

/**
 * Servlet implementation class MemberDuplicationCheckServlet
 */
@WebServlet("/idDuplChk")
public class MemberDuplicationCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberDuplicationCheckServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Encoding from

		// 2. 값 추출
		String memberId = request.getParameter("memberId");

		// 3. 비즈니스 로직 -> 아이디 중복 체크
		MemberService service = new MemberService();
		int result = service.idDuplChk(memberId);

		// 4. 결과 처리 - 피에지 이동시 화면을 다시 그리므로 기존 입력값 사라짐
		// JSP 에서 AJAX 를 이용해서 요청에 대한 응답
		// 응답용 스트림을 이용해서 데이터를 전달
		response.getWriter().print(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
