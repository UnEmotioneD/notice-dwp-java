package kr.or.iei.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.vo.NoticeComment;

/**
 * Servlet implementation class NoticeUpdateCommentServlet
 */
@WebServlet("/notice/updateComment")
public class NoticeUpdateCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeUpdateCommentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String noticeNo = request.getParameter("noticeNo");
		String commentNo = request.getParameter("commentNo");
		String commentContent = request.getParameter("commentContent");

		NoticeComment comment = new NoticeComment();
		comment.setCommentNo(commentNo);
		comment.setCommentRef(noticeNo);
		comment.setCommentContent(commentContent);

		NoticeService service = new NoticeService();
		int result = service.updateComment(comment);

		if (result > 0) {
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "댓글 수정이 완료되었습니다");
			request.setAttribute("icon", "success");
			request.setAttribute("loc", "/notice/view?noticeNo=" + noticeNo + "&commentChk=chk");
		} else {
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "댓글 수정 중 오류가 발생하였습니다");
			request.setAttribute("icon", "error");
			request.setAttribute("loc", "/notice/view?noticeNo=" + noticeNo + "&commentChk=chk");
		}
		request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
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
