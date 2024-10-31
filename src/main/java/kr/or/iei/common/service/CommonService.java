package kr.or.iei.common.service;

import java.sql.Connection;
import java.util.ArrayList;

import kr.or.iei.common.JDBCTemplate;
import kr.or.iei.common.dao.CommonDao;
import kr.or.iei.common.vo.NoticeType;

public class CommonService {
	private CommonDao dao;
	
	public CommonService() {
		dao = new CommonDao();
	}

	public ArrayList<NoticeType> selectNoticeTypeList() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<NoticeType> list = dao.selectNoticeType(conn);
		JDBCTemplate.close(conn);
		return list;
	}

}
