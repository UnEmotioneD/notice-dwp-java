package kr.or.iei.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 기본생성자를 만들어줌
@NoArgsConstructor
// 매개변수 전부 있는 생성자 만들어줌
@AllArgsConstructor
// getter setter toString 만들어줌
@Data

public class Member {
	private String memberNo;
	private String memberId;
	private String memberPw;
	private String memberName;
	private String memberEmail;
	private String memberPhone;
	private String memberAddr;
	private int memberLevel;
	private String enrollDate;
}
