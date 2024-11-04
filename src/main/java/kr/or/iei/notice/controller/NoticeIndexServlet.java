package kr.or.iei.notice.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeIndexServlet
 */
@WebServlet("/notice/index")
public class NoticeIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeIndexServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		NoticeService service = new NoticeService();
		ArrayList<Notice> list = service.selectIndexNoticeList();

//		// Method No.1 using json
//		JSONArray jsonArr = new JSONArray();
//		for (Notice n : list) {
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("noticeCd", n.getNoticeCd());
//			jsonObj.put("noticeTitle", n.getNoticeTitle());
//			jsonObj.put("noticeWriter", n.getNoticeWriter());
//			jsonObj.add(jsonObj);
//		}
//		response.getWriter().print(jsonArr.toJSONString());

		// Method No.1
		Gson gson = new Gson();
		String jsonStr = gson.toJson(list);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json"); // 응답 데이터 형식 지정
		response.getWriter().print(jsonStr);
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
