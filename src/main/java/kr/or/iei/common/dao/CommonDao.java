package kr.or.iei.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.common.vo.NoticeType;

public class CommonDao {

	public ArrayList<NoticeType> selectNoticeType(Connection conn) {
		PreparedStatement pt = null;
		ResultSet rt = null;

		ArrayList<NoticeType> list = new ArrayList<>();
		String query = "SELECT * FROM TBL_NOTICE_TYPE WHERE USE_YN = 'Y'";

		try {
			pt = conn.prepareStatement(query);
			rt = pt.executeQuery();

			while (rt.next()) {
				NoticeType type = new NoticeType();
				type.setNoticeCd(rt.getString("NOTICE_CD"));
				type.setNoticeCdNm(rt.getString("NOTICE_CD_NM"));
				list.add(type);
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