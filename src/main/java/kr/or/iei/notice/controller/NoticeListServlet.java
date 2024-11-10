package kr.or.iei.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.vo.NoticePageData;

/**
 * Servlet implementation class NoticeListServlet
 */
@WebServlet("/notice/list")
public class NoticeListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeListServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 2. Extract value
		String noticeCd = request.getParameter("noticeCd");
		String noticeCdNm = request.getParameter("noticeCdNm");

		int reqPage = request.getParameter("reqPage") == null ? 1 : Integer.parseInt(request.getParameter("reqPage"));

		// 3. Business logic - 전체 게시글 조회
		NoticeService service = new NoticeService();
		NoticePageData pd = service.selectNoticeList(noticeCd, reqPage, noticeCdNm);

		request.setAttribute("noticeList", pd.getList());
		request.setAttribute("pageNavi", pd.getPageNavi());
		request.setAttribute("noticeCd", noticeCd);
		request.setAttribute("noticeCdNm", noticeCdNm);
		request.getRequestDispatcher("/WEB-INF/views/notice/list.jsp").forward(request, response);
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
