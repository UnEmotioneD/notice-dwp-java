package kr.or.iei.notice.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.vo.NoticeFile;

/**
 * Servlet implementation class NoticeDeleteServlet
 */
@WebServlet("/notice/delete")
public class NoticeDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeDeleteServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String noticeNo = request.getParameter("noticeNo");
		String noticeCd = request.getParameter("noticeCd");
		String noticeCdNm = request.getParameter("noticeCdNm");

		System.out.println(noticeNo);
		System.out.println(noticeCd);
		System.out.println(noticeCdNm);

		NoticeService service = new NoticeService();
		ArrayList<NoticeFile> delList = service.deleteNotice(noticeNo);
		if (delList != null) {
			String rootPath = request.getSession().getServletContext().getRealPath("/");

			for (int i = 0; i < delList.size(); i++) {
                //파일 업로드 날짜 == 삭제해야할 폴더명
				String uploadDate = delList.get(i).getFilePath().substring(0, 8);
				String delFilePath = rootPath + "resources/upload/" + uploadDate + "/" + delList.get(i).getFilePath();

				File delFile = new File(delFilePath);
				// 해당 경로에 삭제 파일이 존재하면
				if (delFile.exists()) {
					delFile.delete();
				}
			}
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "게시글이 정상적으로 삭제되었습니다.");
			request.setAttribute("icon", "success");
			request.setAttribute("loc", "/notice/list?page=1&noticeCd=" + noticeCd + "&noticeCdNm=" + noticeCdNm);
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
