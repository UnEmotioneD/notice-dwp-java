package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.member.model.service.MemberService;

/**
 * Servlet implementation class MemberSrchInfoIdServlet
 */
@WebServlet("/member/srchInfo")
public class MemberSrchInfoIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberSrchInfoIdServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String memberEmail = request.getParameter("memberEmail");
		
		MemberService service = new MemberService();
		String memberId = service.srchInfoId(memberEmail);

		if (memberId != null) {
			
			// un*******ed 처럼 보여주기
			int idLen = memberId.length(); // 조회한 아이디의 길이

			String first = memberId.substring(0, 2); // 아이디 첫 2자리
			String last = memberId.substring(idLen - 2); // 아이디 마지막 2자리
			String marker = "*".repeat(idLen - 4); // (아이디 길이) - 4 만큼 (*) 생성
			
			memberId = first + marker + last;

		} else {
			memberId = "";

		}
		response.getWriter().print(memberId);
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
