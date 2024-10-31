package kr.or.iei.notice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeComment {

	private String commentNo;
	private String commentRef;
	private String commentContent;
	private String commentWriter;
	private String commentDate;
}
