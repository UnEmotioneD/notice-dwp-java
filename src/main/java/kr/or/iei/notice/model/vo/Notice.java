package kr.or.iei.notice.model.vo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notice {
	private String noticeNo;
	private String noticeCd;
	private String noticeTitle;
	private String noticeWriter;
	private String noticeContent;
	private String noticeDate;
	private int readCount;

	// 종속 데이터 추가 변수 추가
	private String noticeCdNm;
	private ArrayList<NoticeFile> fileList;
	private ArrayList<NoticeComment> commentList;
}
