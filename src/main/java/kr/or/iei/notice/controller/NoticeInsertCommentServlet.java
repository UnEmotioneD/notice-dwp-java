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
 * Servlet implementation class NoticeInsertCommentServlet
 */
@WebServlet("/notice/insertComment")
public class NoticeInsertCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeInsertCommentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String commentRef = request.getParameter("commentRef");
		String commentWriter = request.getParameter("commentWriter");
		String commentContent = request.getParameter("commentContent");

		NoticeComment comment = new NoticeComment();
		comment.setCommentRef(commentRef);
		comment.setCommentWriter(commentWriter);
		comment.setCommentContent(commentContent);

		NoticeService service = new NoticeService();
		int result = service.insertComment(comment);

		if (result > 0) {
			request.setAttribute("icon", "알림");
			request.setAttribute("msg", "댓글이 작성되었습니다");
			request.setAttribute("icon", "success");
			request.setAttribute("loc", "/notice/view?noticeNo=" + commentRef + "&commentChk=chk");
	 	} else {
			request.setAttribute("icon", "실패");
			request.setAttribute("msg", "댓글 작성중 오류가 발생하였습니다");
			request.setAttribute("icon", "error");
			request.setAttribute("loc", "/notice/view?noticeNo=" + commentRef + "&commentChk=chk");
	 	}
		
		request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
