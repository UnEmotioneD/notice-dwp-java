package kr.or.iei.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.notice.model.dao.NoticeDao;
import kr.or.iei.notice.model.vo.Notice;
import kr.or.iei.notice.model.vo.NoticeComment;
import kr.or.iei.notice.model.vo.NoticeFile;
import kr.or.iei.notice.model.vo.NoticePageData;

public class NoticeService {

	private NoticeDao dao;

	public NoticeService() {
		dao = new NoticeDao();
	}

	public NoticePageData selectNoticeList(String noticeCd, int reqPage, String noticeCdNm) {
		Connection conn = JDBCTemplate.getConnection();

		// 한 페이지에 보여줄 게시글의 갯수
		int viewNoticeCnt = 10;
		/*
		 * 요청 페이지가 1 번 페이지 일때, -> start : 1, end : 10 요청 페이지가 2 번 페이지 일때, -> start : 11,
		 * end : 20 요청 페이지가 3 번 페이지 일때, -> start : 21, end : 30 ...
		 * 
		 * 한번에 보여줄 게시글이 5개 일때 요청 페이지가 1 번 페이지 일때, -> start : 1, end : 5 요청 페이지가 2 번 페이지
		 * 일때, -> start : 6, end : 10 요청 페이지가 3 번 페이지 일때, -> start : 11, end : 15 ...
		 */
		int end = reqPage * viewNoticeCnt;
		int start = end - viewNoticeCnt + 1;

		ArrayList<Notice> list = dao.selectNoticeList(conn, noticeCd, start, end);

		// 전체 게시물의 갯수
		int totCnt = dao.selectNoticeCount(conn, noticeCd);

		// 전체 페이지 갯수
		int totPage = 0;

		totPage = totCnt / viewNoticeCnt;

		if (totCnt % viewNoticeCnt != 0) {
			totPage += 1;
		}

		// 페이지 하단에 보여질 페이지 네비게이션 사이즈
		int pageNaviSize = 5;

		/*
		 * 페이지 네비게이션 시작 번호 요청 페이지가 1 페이지이고 페이지 네비게이션 사이즈가 5 일때 == 1 2 3 4 5 요청 페이지가 4
		 * 페이지이고 페이지 네비게이션 사이즈가 5 일때 == 1 2 3 4 5 요청 페이지가 6 페이지이고 페이지 네비게이션 사이즈가 5 일때 ==
		 * 6 7 8 9 10
		 */
		int pageNo = ((reqPage - 1) / pageNaviSize) * pageNaviSize + 1;

		// 페이지 네비개이션 HTML 태그 생성
		String pageNavi = "<ul class='pagination circle-style'>";

		if (pageNo != 1) {
			// 6 7 8 9 10 또는 11 12 13 14 15 or ...
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/notice/list?reqPage=" + (pageNo - 1) + "&noticeCd=" + noticeCd
					+ "&noticeCdNm=" + noticeCdNm + "'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}

		// 페이지 네비게이션 사이즈만큼 반복하며 태그 생성
		for (int i = 0; i < pageNaviSize; i++) {
			pageNavi += "<li>";

			// 선택한 페이지와 선택하지 않은 페이지를 시각적으로 다르게 표현
			if (reqPage == pageNo) {
				pageNavi += "<a class='page-item active-page' href='/notice/list?reqPage='" + pageNo + "&noticeCd="
						+ noticeCd + "&noticeCdNm=" + noticeCdNm + "'>";
			} else {
				pageNavi += "<a class='page-item' href='/notice/list?reqPage='" + pageNo + "&noticeCd=" + noticeCd
						+ "&noticeCdNm=" + noticeCdNm + "'>";
			}

			pageNavi += pageNo + "</a></li>";
			pageNo++;

			// 쓸데 없는 페이지 번호는 만들지 않도록
			if (pageNo > totPage) {
				break;
			}
		}

		// 시작번호 <= 전체 페이지 갯수
		if (pageNo <= totPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item href='/notice/list?reqPage='" + pageNo + "&noticeCd" + noticeCd
					+ "&noticeCdNm" + noticeCdNm + "'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}

		pageNavi += "</ul>";

		NoticePageData pd = new NoticePageData();
		pd.setList(list);
		pd.setPageNavi(pageNavi);

		JDBCTemplate.close(conn);
		return pd;
	}

	public int insertNotice(Notice notice, ArrayList<NoticeFile> fileList) {
		Connection conn = JDBCTemplate.getConnection();

		// 1. 게시글 번호 조회 (파일 등록시에도, 게시글 번호가 필요)
		String noticeNo = dao.selectNoticeNo(conn);

		// 2. 게시글 등록
		notice.setNoticeNo(noticeNo);
		int result = dao.insertNotice(conn, notice);

		if (result > 0) {
			boolean fileChk = true;
			for (NoticeFile file : fileList) {
				file.setNoticeNo(noticeNo);
				result = dao.insertNoticeFile(conn, file);

				if (result < 1) {
					JDBCTemplate.rollback(conn);
					fileChk = false;
					break;
				}
			}

			if (fileChk) {
				JDBCTemplate.commit(conn);
			}

		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public Notice selectOneNotice(String noticeNo, String commentChk) {
		Connection conn = JDBCTemplate.getConnection();
		Notice n = dao.selectOneNotice(conn, noticeNo);

		if (n != null) {
			int result = 0;

			// commnetChk == null 인 것은 댓글을 작성하고 상세보기 이동하는 경우를 제외한 모든 요청
			if (commentChk == null) {
				result = dao.updateReadCount(conn, noticeNo);
			}

			// commentChk != null 인 경우는 댓글을 작성하고 상세보기 이동하는 경우에도 파일 정보를 select 할 수 있도록
			if (result > 0 || commentChk != null) {
				JDBCTemplate.commit(conn);

				// 파일 리스트, 댓글 리스트를 모두 1개의 게시글에 종속적인 데이터이므로 별도의 클래스를 생성하지 않고 Notice 클래스에 변수로 추가
				ArrayList<NoticeFile> fileList = dao.selectNoticeFileList(conn, noticeNo);
				n.setFileList(fileList);

				ArrayList<NoticeComment> commentList = dao.selectCommentList(conn, noticeNo);
				n.setCommentList(commentList);

			} else {
				JDBCTemplate.rollback(conn);
			}

		}
		JDBCTemplate.close(conn);

		return n;
	}

	public Notice getOneNotice(String noticeNo) {
		Connection conn = JDBCTemplate.getConnection();
		Notice n = dao.selectOneNotice(conn, noticeNo);

		if (n != null) {
			ArrayList<NoticeFile> fileList = dao.selectNoticeFileList(conn, noticeNo);
			n.setFileList(fileList);
		}

		JDBCTemplate.close(conn);
		return n;
	}

	public ArrayList<NoticeFile> updateNotice(Notice notice, ArrayList<NoticeFile> addFileList,
			String[] delFileNoList) {
		Connection conn = JDBCTemplate.getConnection();

		// 게시글 정보 수정
		int result = dao.updateNotice(conn, notice);

		ArrayList<NoticeFile> preFileList = new ArrayList<>();

		if (result > 0) {
			// DB 삭제 이전에 기존 파일 리스트 조회
			preFileList = dao.selectNoticeFileList(conn, notice.getNoticeNo());

			if (delFileNoList != null) {
				// 사용자가 삭제 요청한 파일을 DB 에서 삭제
				String delFileNoStr = "";

				for (String s : delFileNoList) {
					delFileNoStr += s + "|";
				}

				/*
				 * 기존 파일 갯수가 5일 때 4 -> 3 -> 2 -> 1 -> 0
				 */
				// 기존 파일리스트 중 삭제 대상 파일인 경우
				for (int i = preFileList.size() - 1; i >= 0; i--) {
					if (delFileNoStr.indexOf(preFileList.get(i).getFileNo()) > -1) {
						// DB 삭제
						result += dao.deleteNoticeFile(conn, preFileList.get(i).getFileNo());

					} else {
						// 삭제 대상이 아님 (서블릿으로 preFileList 를 리턴할 때 삭제 대상파일만 리턴할것임)
						preFileList.remove(i);

					}

				}
			}

			// 추가 파일 DB 등록
			for (NoticeFile addFile : addFileList) {
				result += dao.insertNoticeFile(conn, addFile);
			}

		}

		// 변경된 행의 수
		int updTotCnt = delFileNoList == null ? addFileList.size() + 1 : delFileNoList.length + addFileList.size() + 1;

		if (updTotCnt == result) {
			JDBCTemplate.commit(conn);
			JDBCTemplate.close(conn);
			return preFileList;

		} else {
			JDBCTemplate.rollback(conn);
			JDBCTemplate.close(conn);
			return null;
		}
	}

	public int insertComment(NoticeComment comment) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.insertComment(conn, comment);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public int deleteComment(String commentNo) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.deleteComment(conn, commentNo);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public int updateComment(NoticeComment comment) {
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.updateComment(conn, comment);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

	public ArrayList<Notice> selectIndexNoticeList() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Notice> list = dao.selectIndexanoticeList(conn);
		JDBCTemplate.close(conn);
		return list;
	}

}
