package kr.or.iei.notice.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.notice.model.vo.Notice;
import kr.or.iei.notice.model.vo.NoticeComment;
import kr.or.iei.notice.model.vo.NoticeFile;

public class NoticeDao {

	public ArrayList<Notice> selectNoticeList(Connection conn, String noticeCd, int start, int end) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		ArrayList<Notice> list = new ArrayList<>();
		String query = "SELECT * FROM (SELECT ROWNUM AS RNUM, A.* FROM ( SELECT A.* FROM TBL_NOTICE A WHERE NOTICE_CD = ? ORDER BY NOTICE_NO DESC ) A ) WHERE RNUM BETWEEN ? AND ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeCd);
			pt.setInt(2, start);
			pt.setInt(3, end);

			rt = pt.executeQuery();

			while (rt.next()) {
				Notice n = new Notice();

				n.setNoticeNo(rt.getString("NOTICE_NO"));
				n.setNoticeCd(rt.getString("NOTICE_CD"));
				n.setNoticeTitle(rt.getString("NOTICE_TITLE"));
				n.setNoticeContent(rt.getString("NOTICE_CONTENT"));
				n.setNoticeWriter(rt.getString("NOTICE_WRITER"));
				n.setNoticeDate(rt.getString("NOTICE_DATE"));
				n.setReadCount(rt.getInt("READ_COUNT"));
				list.add(n);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}

		return list;
	}

	public int selectNoticeCount(Connection conn, String noticeCd) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		String query = "SELECT COUNT(*) CNT FROM TBL_NOTICE WHERE NOTICE_CD = ?";

		int totCnt = 0;

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeCd);
			rt = pt.executeQuery();

			if (rt.next()) {
				totCnt = rt.getInt("cnt");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}

		return totCnt;
	}

	public String selectNoticeNo(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String query = "select to_char(sysdate, 'yymmdd') || lpad(seq_notice.nextval, 4, '0') as notice_no from dual";
		String noticeNo = "";

		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			rset.next();
			noticeNo = rset.getString("notice_no");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return noticeNo;
	}

	public int insertNotice(Connection conn, Notice notice) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "INSERT INTO TBL_NOTICE VALUES (?, ?, ?, ?, ?, SYSDATE, DEFAULT)";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, notice.getNoticeNo());
			pt.setString(2, notice.getNoticeCd());
			pt.setString(3, notice.getNoticeTitle());
			pt.setString(4, notice.getNoticeContent());
			pt.setString(5, notice.getNoticeWriter());
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int insertNoticeFile(Connection conn, NoticeFile file) {
		PreparedStatement pt = null;
		String query = "INSERT INTO TBL_NOTICE_FILE VALUES (TO_CHAR(SYSDATE, 'yymmdd') || LPAD(SEQ_NOTICE_FILE.NEXTVAL, 4,'0'), ?, ?, ?)";
		int result = 0;

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, file.getNoticeNo());
			pt.setString(2, file.getFileName());
			pt.setString(3, file.getFilePath());
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public Notice selectOneNotice(Connection conn, String noticeNo) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		Notice n = null;

		String query = "SELECT A.*, C.NOTICE_CD_NM FROM TBL_NOTICE A, TBL_NOTICE_TYPE C WHERE A.NOTICE_NO = ? AND A.NOTICE_CD = C.NOTICE_CD";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeNo);
			rt = pt.executeQuery();

			if (rt.next()) {
				n = new Notice();
				n.setNoticeNo(rt.getString("notice_no"));
				n.setNoticeCd(rt.getString("notice_cd"));
				n.setNoticeCdNm(rt.getString("notice_cd_nm"));
				n.setNoticeTitle(rt.getString("notice_title"));
				n.setNoticeWriter(rt.getString("notice_writer"));
				n.setNoticeContent(rt.getString("notice_content"));
				n.setNoticeDate(rt.getString("notice_date"));
				n.setReadCount(rt.getInt("read_count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}

		return n;
	}

	public int updateReadCount(Connection conn, String noticeNo) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "UPDATE TBL_NOTICE SET READ_COUNT = READ_COUNT + 1 WHERE NOTICE_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeNo);

			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public ArrayList<NoticeFile> selectNoticeFileList(Connection conn, String noticeNo) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		String query = "SELECT * FROM TBL_NOTICE_FILE WHERE NOTICE_NO = ?";
		ArrayList<NoticeFile> fileList = new ArrayList<>();

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeNo);
			rt = pt.executeQuery();
			while (rt.next()) {
				NoticeFile file = new NoticeFile();
				file.setFileNo(rt.getString("FILE_NO"));
				file.setNoticeNo(rt.getString("NOTICE_NO"));
				file.setFileName(rt.getString("FILE_NAME"));
				file.setFilePath(rt.getString("FILE_PATH"));
				fileList.add(file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return fileList;
	}

	public int updateNotice(Connection conn, Notice notice) {
		PreparedStatement pt = null;
		int result = 0;
		// 상세보기 에서 돌아왔을때 조회수가 늘어나는걸 막기위해서 read_count -1
		String query = "UPDATE TBL_NOTICE SET NOTICE_TITLE = ?, NOTICE_CONTENT = ?, READ_COUNT = READ_COUNT - 1 WHERE NOTICE_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, notice.getNoticeTitle());
			pt.setString(2, notice.getNoticeContent());
			pt.setString(3, notice.getNoticeNo());
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int deleteNoticeFile(Connection conn, String fileNo) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "DELETE FROM TBL_NOTICE_FILE WHERE FILE_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, fileNo);
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int deleteNotice(Connection conn, String noticeNo) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "DELETE FROM TBL_NOTICE WHERE NOTICE_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeNo);
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int insertComment(Connection conn, NoticeComment comment) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "INSERT INTO TBL_NOTICE_COMMENT VALUES(TO_CHAR(SYSDATE,'yymmdd') || LPAD(SEQ_NOTICE_COMMENT.NEXTVAL,4,'0'), ?, ?, SYSDATE, ?)";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, comment.getCommentWriter());
			pt.setString(2, comment.getCommentContent());
			pt.setString(3, comment.getCommentRef());
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public ArrayList<NoticeComment> selectCommentList(Connection conn, String noticeNo) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		String query = "SELECT * FROM TBL_NOTICE_COMMENT WHERE COMMENT_REF = ? ORDER BY COMMENT_DATE DESC";
		ArrayList<NoticeComment> list = new ArrayList<>();

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, noticeNo);
			rt = pt.executeQuery();

			while (rt.next()) {
				NoticeComment c = new NoticeComment();
				c.setCommentNo(rt.getString("COMMENT_NO"));
				c.setCommentWriter(rt.getString("COMMENT_WRITER"));
				c.setCommentContent(rt.getString("COMMENT_CONTENT"));
				c.setCommentRef(rt.getString("COMMENT_REF"));
				c.setCommentDate(rt.getString("COMMENT_DATE"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return list;
	}

	public int deleteComment(Connection conn, String commentNo) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "DELETE FROM TBL_NOTICE_COMMENT WHERE COMMENT_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, commentNo);
			result = pt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public int updateComment(Connection conn, NoticeComment comment) {
		PreparedStatement pt = null;
		int result = 0;
		String query = "UPDATE TBL_NOTICE_COMMENT SET COMMENT_CONTENT = ? WHERE COMMENT_NO = ?";

		try {
			pt = conn.prepareStatement(query);
			pt.setString(1, comment.getCommentContent());
			pt.setString(2, comment.getCommentNo());
			result = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pt);
		}
		return result;
	}

	public ArrayList<Notice> selectIndexanoticeList(Connection conn) {
		PreparedStatement pt = null;
		ResultSet rt = null;
		
		// notice_cd 별로 그룹을 지어 행번호를 조회
		String query = "SELECT * FROM ( SELECT ROW_NUMBER() OVER ( PARTITION BY NOTICE_CD ORDER BY NOTICE_DATE DESC ) AS RNUM, A.* FROM TBL_NOTICE A ) WHERE RNUM < = 5";
		ArrayList<Notice> list = new ArrayList<Notice>();

		try {
			pt = conn.prepareStatement(query);
			rt = pt.executeQuery();

			while (rt.next()) {
				Notice n = new Notice();
				n.setNoticeNo(rt.getString("NOTICE_NO"));
				n.setNoticeCd(rt.getString("NOTICE_CD"));
				n.setNoticeTitle(rt.getString("NOTICE_TITLE"));
				n.setNoticeContent(rt.getString("NOTICE_CONTENT"));
				n.setNoticeWriter(rt.getString("NOTICE_WRITER"));
				n.setNoticeDate(rt.getString("NOTICE_DATE"));
				n.setReadCount(rt.getInt("READ_COUNT"));

				list.add(n);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rt);
			JDBCTemplate.close(pt);
		}
		return list;
	}

}
