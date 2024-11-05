package kr.or.iei.member.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.iei.member.model.service.MemberService;

/**
 * Servlet implementation class MemberSrchInfoPwServlet
 */
@WebServlet("/member/srchInfoPw")
public class MemberSrchInfoPwServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberSrchInfoPwServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String memberId = request.getParameter("memberId");
		String memberEmail = request.getParameter("memberEmail");

		MemberService service = new MemberService();
		String toEmail = service.srchInfoPw(memberId, memberEmail);

		if (toEmail != null) {
			// 임시 비밀번호 10자리 생성
			String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String lower = "abcdefghijklmnopqrstuvwxyz";
			String digit = "0123456789";
			String special = "!@#$";
			// 대문자 소문자 숫자 특수문자가 최소한 하나씩은 임시 비밀번호에 추가 되도록
			String allStr = upper + lower + digit + special;

			// 난수 발생 클래스
			SecureRandom random = new SecureRandom();
			// 임시 비밀번호
			StringBuilder ranPw = new StringBuilder();

			ranPw.append(upper.charAt(random.nextInt(upper.length()))); // 대문자 중 1개 문자
			ranPw.append(lower.charAt(random.nextInt(lower.length())));
			ranPw.append(digit.charAt(random.nextInt(digit.length())));
			ranPw.append(special.charAt(random.nextInt(special.length())));
			// 나머지 6자리는 allStr 에서 추출
			for (int i = 0; i < 6; i++) {
				ranPw.append(allStr.charAt(random.nextInt(allStr.length())));
			}
			// 근데 여기까지는 추측 가능함 (첫 4글자의 형태가 고정이기때문에)
			char[] allChars = ranPw.toString().toCharArray();

			for (int i = 0; i < allChars.length; i++) {
				int ranIdx = random.nextInt(allChars.length); // 0 ~ 9 중 난수 발생

				// 현재 i 번쨰 인덱스 값이랑, 난수번째에 있는 요소 값이랑 바꿔치기
				char tmp = allChars[i];
				allChars[i] = allChars[ranIdx];
				allChars[ranIdx] = tmp;
			}

			// 최종적으로 임시 비밀번호가 들어있는 allChars 를 String 으로 변환
			String newPw = new String(allChars);

			// DB 최신화
			int result = service.updateMemberPw(memberId, memberEmail);

			if (result > 0) {
				// DB 최신화 이후 변경된 임시 비밀번호를 회원강ㅂ시 입력한 이메일로 전송

				// 1. 환경 설정 정보 (네이버 이메일 보내기랑 똑같음)
				Properties prop = new Properties();
				prop.put("mail.smtp.host", "smtp.naver.com");
				prop.put("mail.smtp.port", 465);
				prop.put("mail.smtp.auth", "true");
				prop.put("mail.smtp.ssl.enable", "true");
				prop.put("mail.smtp.ssl.trust", "smtp.naver.com");

				// 2. 세션 설정 및 인증 정보 설정
				Session session = Session.getDefaultInstance(prop, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("unemotioned@naver.com", "Blackdward9");
					}
				});

				// 3. 이메일 관련 정보 세팅
				MimeMessage msg = new MimeMessage(session);

				try {
					msg.setSentDate(new Date());

					msg.setFrom(new InternetAddress("unemotioned@naver.com", "From UnEmotioneD"));

					InternetAddress to = new InternetAddress(toEmail);
					msg.setRecipient(Message.RecipientType.TO, to);

					msg.setSubject("임시 비밀번호 발급 안내");
					msg.setContent(
							"회원님의 임시 비밀번호는 [<span style='color:red; font-weight:bold;'>" + newPw + "</span>] 입니다",
							"text/html; charset=UTF-8");

					Transport.send(msg);
				} catch (MessagingException e) {
					e.printStackTrace();
				}

				// 임시 비밀번호 생성 후 이메일 전송 완료
				response.getWriter().print("foo");
			} else {
				// DB 최신화가 정상적으로 이루어지지 않았을때
			}
		} else {
			// 입력한 정보와 일치하는 회원이 존재하지 않을때
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
