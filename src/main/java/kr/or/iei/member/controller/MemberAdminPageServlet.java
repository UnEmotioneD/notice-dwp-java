package kr.or.iei.member.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.member.model.vo.Member;

/**
 * Servlet implementation class MemberAdminPageServlet
 */
@WebServlet("/member/adminPage")
public class MemberAdminPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberAdminPageServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session != null) {
			Member loginMember = (Member) session.getAttribute("loginMember");

			if (1 != loginMember.getMemberLevel()) {
				request.setAttribute("title", "알림");
				request.setAttribute("msg", "해당 메뉴에 대한 접속 권한이 없습니다");
				request.setAttribute("icon", "error");
				request.setAttribute("loc", "/member/mypage");

				request.getRequestDispatcher("/WEB-INF/views/commn/msg.jsp").forward(request, response);
				return;
			}
		}

		// 3. Business logic
		MemberService service = new MemberService();
		ArrayList<Member> list = service.selectAllMember();

		// 4. Process results
		request.setAttribute("memberList", list);
		request.getRequestDispatcher("/WEB-INF/views/member/adminPage.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
