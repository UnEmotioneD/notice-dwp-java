package kr.or.iei.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.member.model.vo.Member;

/**
 * Servlet implementation class MemberPwChgServlet
 */
@WebServlet("/member/pwChg")
public class MemberPwChgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberPwChgServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. Encoding
		// 2. Extract values
		String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		String newMemberPw = request.getParameter("newMemberPw");

		// 3. Business logic -- 비밀번호 변경

		// 3.1) 사용자가 입력한 기존 비밀번호와 등록되어있는 비밀번호가 같은지 비교

		/*
		 * 로그인 시 등록한 세션의 지속 시간은 10분으로 설정하였음 세션이 만료외었을 때 아래 코는 null.getAttribute() 로
		 */

		// NullapointerException 발생할 우려가 있다
		// Member loginMember =
		// (Member)request.getSession(false).getAttribute("loginMember"); // 세션에 등록되어있는
		// 기존 회원 정보

		HttpSession session = request.getSession(false);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp");

		if (session != null) {
			Member loginMember = (Member) session.getAttribute("loginMember");

//			if (!loginMember.getMemberPw().equals(memberPw)) {
//				request.setAttribute("title", "실패");
//				request.setAttribute("msg", "기존 비밀번호가 일치하지 않습니다");
//				request.setAttribute("icon", "error");
//				request.setAttribute("callback", "self.close();"); // 이동한 msg.jsp 에서 알림창을 띄워주고난 이후에 실행할 함수 등록
//
//				view.forward(request, response);
//				return;
//			}

			// After password encryption
			if (!BCrypt.checkpw(memberPw, loginMember.getMemberPw())) {
				request.setAttribute("title", "실패");
				request.setAttribute("msg", "기존 비밀번호가 일치하지 않습니다");
				request.setAttribute("icon", "error");
				request.setAttribute("callback", "self.close();"); // 이동한 msg.jsp 에서 알림창을 띄워주고난 이후에 실행할 함수 등록

				view.forward(request, response);
				return;
			}

			MemberService service = new MemberService();
			int result = service.updateMemberPw(memberId, newMemberPw);

			// 4. Process results
			if (result > 0) {
				request.setAttribute("title", "알림");
				request.setAttribute("msg", "비밀번호가 변경되었습니다. 변경된 비밀번호로 다시 로그인하세요");
				request.setAttribute("icon", "success");
				// loc 를 사용하면 팝업창에서 로그인창으로 이동하게 된다
				// bslsh 를 작성해주면 물자열로 동작한다
				request.setAttribute("callback", "self.close();window.opener.location.href=\'/member/loginFrm\';");
				// 세션 정보를 소멸시킨다
				session.invalidate();
			} else {
				request.setAttribute("title", "실패");
				request.setAttribute("msg", "비밀번호 변경 중, 오류가 발생하였습니다");
				request.setAttribute("icon", "error");
				request.setAttribute("callback", "self.close();");
			}
			view.forward(request, response);
		}
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
