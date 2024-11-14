package kr.or.iei.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeUpdateFrmServlet
 */
@WebServlet("/notice/updateFrm")
public class NoticeUpdateFrmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeUpdateFrmServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String noticeNo = request.getParameter("noticeNo");

		NoticeService service = new NoticeService();

		// 기존 상세보기 이동 시 작성된 메소드 호춯라면 조회수가 증가되므로 별도 메소드 생성하여 호출
		Notice n = service.getOneNotice(noticeNo);

		request.setAttribute("notice", n);
		request.getRequestDispatcher("/WEB-INF/views/notice/updateFrm.jsp").forward(request, response);
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
