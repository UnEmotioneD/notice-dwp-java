package kr.or.iei.notice.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import kr.or.iei.common.vo.MyRenamePolicy;
import kr.or.iei.notice.model.service.NoticeService;
import kr.or.iei.notice.model.vo.Notice;
import kr.or.iei.notice.model.vo.NoticeFile;

/**
 * Servlet implementation class NoticeUpdateServlet
 */
@WebServlet("/notice/update")
public class NoticeUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeUpdateServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 추가 파일 처리
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String savePath = rootPath + "resources/upload/" + today + "/"; // 파일 저장 경로

		File dir = new File(savePath);

		// 오늘 날짜 폴더가 생성되어 있지 않을때
		if (!dir.exists()) {
			dir.mkdir();
		}

		int maxSize = 1024 * 1024 * 10;
		MultipartRequest mRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new MyRenamePolicy());

		// 게시글 정보
		String noticeNo = mRequest.getParameter("noticeNo");
		String noticeTitle = mRequest.getParameter("noticeTitle");
		String noticeContent = mRequest.getParameter("noticeContent");

		// type 이 file 인 태그들
		Enumeration<String> files = mRequest.getFileNames();

		ArrayList<NoticeFile> addFileList = new ArrayList<>();

		while (files.hasMoreElements()) {
			String name = files.nextElement();

			String fileName = mRequest.getOriginalFileName(name);
			String filePath = mRequest.getFilesystemName(fileName);

			// type 이 file 인 태그들 중에 없로드 되지 않은것을 제외
			if (filePath != null) {
				NoticeFile file = new NoticeFile();

				file.setNoticeNo(noticeNo);
				file.setFileName(fileName);
				file.setFilePath(filePath);

				addFileList.add(file);
			}
		}

		// 삭제 파일 리스트 (jsp 에서 아이콘 클릭시, form 태그 하위에 추가해준 hidden 속성을 가진 input 태그들)
		String[] delFileNoList = mRequest.getParameterValues("delFileNo");

		Notice notice = new Notice();
		notice.setNoticeNo(noticeNo);
		notice.setNoticeTitle(noticeTitle);
		notice.setNoticeContent(noticeContent);

		NoticeService service = new NoticeService();
		ArrayList<NoticeFile> delFileList = service.updateNotice(notice, addFileList, delFileNoList);

		if (delFileList == null) {
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "수정 중 오류가 발생하였습니다");
			request.setAttribute("icon", "error");
			request.setAttribute("loc", "/notice/updateFrm?noticeNo=" + noticeNo);
		} else {
			// 서비스에서 모든 insert, update, delete 가 정상적으로 이루어진 경우
			for (int i = 0; i < delFileList.size(); i++) {
				// 파일 업로드 날짜 == 삭제해야할 파일의 폴더명
				String uploadDate = delFileList.get(i).getFilePath().substring(0, 8);
				String delFilePath = rootPath + "resources/upload/" + uploadDate + "/"
						+ delFileList.get(i).getFilePath();

				File delFile = new File(delFilePath);

				// 해당 경로에 삭제 파일이 존재하면
				if (delFile.exists()) {
					delFile.delete();
				}
			}
			request.setAttribute("title", "알림");
			request.setAttribute("msg", "게시글이 정상적으로 수정되었습니다");
			request.setAttribute("icon", "success");
			request.setAttribute("loc", "/notice/view?noticeNo=" + noticeNo);
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
