package kr.or.iei.notice.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NoticeFileDownServlet
 */
@WebServlet("/notice/fileDown")
public class NoticeFileDownServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NoticeFileDownServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		String filePath = request.getParameter("filePath");

		// 파일이름에서 앞에 8자리
		String writeDate = filePath.substring(0, 8);
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String savePath = rootPath + "resources/upload/" + writeDate + "/";

		File file = new File(savePath + filePath);

		if (file.exists()) {
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try {
				FileInputStream fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);

				// MIME-TPYE : 파일 형식과 파일이 어떻게 처리되어야 하는지를 나타내는 식별자
				response.setContentType("application/octect-stream");

				String resFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

				// 브라우저에게 파일을 다운로드할것을 지시한다.
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", resFileName));

				ServletOutputStream sos = response.getOutputStream();

				bos = new BufferedOutputStream(sos);

				while (true) {
					int read = bis.read();

					if (read == -1) {
						break;
					} else {
						bos.write(read);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				bis.close();
				bos.close();
			}
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
