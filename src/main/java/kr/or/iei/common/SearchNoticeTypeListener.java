package kr.or.iei.common;

import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kr.or.iei.common.service.CommonService;
import kr.or.iei.common.vo.NoticeType;

/**
 * Application Lifecycle Listener implementation class SearchNoticeTypeListener
 *
 */

public class SearchNoticeTypeListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public SearchNoticeTypeListener() {
		
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	// 소멸될때
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	// 생성될때
	public void contextInitialized(ServletContextEvent sce) {
		CommonService commonService = new CommonService();
		ArrayList<NoticeType> typeList = commonService.selectNoticeTypeList();

		sce.getServletContext().setAttribute("noticeTypeList", typeList);
	}

}
