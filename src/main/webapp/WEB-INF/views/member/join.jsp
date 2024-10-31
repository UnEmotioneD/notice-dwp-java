<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>join.jsp file</title>
<style>
.join-wrap {
	width: 500px;
	padding: 20px;
	margin: 0 auto;
}

.join-wrap .input-wrap {
	padding: 15px 30px;
}

.join-wrap .login-button-box {
	padding: 20px 30px;
	display: flex;
	justify-content: center;
}

.join-wrap .join-button-box>bottom {
	width: 100%;
}
</style>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content">
			<section class="section join-wrap">

				<div class="page-title">회원가입</div>
				<form action="/member/join" method="post" autocomplete="off"
					onsubmit="return joinValidate()">

					<div class="input-wrap">
						<div class="input-title">
							<label for=memberId>아이디</label>
						</div>
						<div class="input-item">
							<input type="text" id="memberId" name="memberId"
								placeholder="영어, 숫자 8~20 글자" maxlength="20">
							<button type="button" id="idDuplChkBtn" class="btn-primary">중복체크</button>
						</div>
						<p id="idMessage" class="input-msg"></p>
					</div>

					<div class="input-wrap">
						<div class="input-title">
							<label for="memberPw">비밀번호</label>
						</div>
						<div class="input-item">
							<input type="password" id="memberPw" name="memberPw"
								placeholder="영어, 숫자, 특수문자(!, @, #, $) 6~30 글자" maxlength="30">
						</div>
					</div>

					<div class="input-wrap">
						<div class="input-title">
							<label for="memberPwConfirm">비밀번호 확인</label>
						</div>
						<div class=input-item>
							<input type="password" id="memberPwConfirm" maxlength="30">
						</div>
						<p id="pwMessage" class="input-msg"></p>
					</div>

					<div class="input-wrap">
						<div class="input-title">
							<label for="memberEmail">이메일</label>
						</div>
						<div class="input-item">
							<input type="email" id="memberEmail" name="memberEmail">
						</div>
					</div>

					<div class="input-wrap">
						<div class="input-title">
							<label for="memberName">이름</label>
						</div>
						<div class="input-item">
							<input type="text" id="memberName" name="memberName"
								placeholder="한글 2~10 글자" maxlength="10">
						</div>
						<p id="nameMessage" class="input-msg"></p>
					</div>

					<div class="input-wrap">
						<div class="input-title">
							<label for="memberPhone">전화번호</label>
						</div>
						<div class="input-item">
							<input type="text" id="memberPhone" name="memberPhone"
								placeholder="전화번호(010-0000-0000)" maxlength="13">
						</div>
						<p id="phoneMessage" class="input-msg"></p>
					</div>

					<div class="input-wrap">
						<div class="input-title">
							<label for="memberAddr">주소</label>
						</div>
						<div class="input-item">
							<input type="text" id="memberAddr" name="memberAddr"
								maxlength="200">
						</div>
					</div>

					<div class="login-button-box">
						<button type="submit" class="btn-primary lg">회원가입</button>
					</div>
				</form>
			</section>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	</div>
	<script>
		// submit 동작시 아래 객체의 모든 객체에 대해서 true 인지 검사할것이다
		const checkObj = {
			"memberId" : false,
			"idDuplChk" : false,
			"memberPw" : false,
			"memberPwConfirm" : false,
			"memberName" : false,
			"memberPhone" : false,
		};

		const memberId = $("#memberId");
		const idMessage = $("#idMessage");

		memberId.on("input", function() {
			/*
			checkObj.idDuplChk = false; 를 하지 않으면
			1. 중복검사 통과하고
			2. 중복되는 아이디로 바꾸고 (중복체크 하지 않고)
			3. 회원가입 버튼을 누르면 true 값이 남아있다
			 */
			checkObj.idDuplChk = false;

			idMessage.removeClass("valid");
			idMessage.removeClass("invalid");

			const regExp = /^[a-zA-Z0-9]{8,20}$/;

			if (regExp.test($(this).val())) {
				idMessage.html("Valid");
				idMessage.addClass("valid");

				checkObj.memberId = true;
			} else {
				idMessage.html("영어, 숫자 8~20 글자 사이로 입력하시오.");
				idMessage.addClass("invalid");

				checkObj.memberId = false;
			}
		});

		$("#idDuplChkBtn").on("click", function() {
			// 요청 보내기전에 front-end 에서 처리
			if (!checkObj.memberId) {
				msg("알림", "유효한 아이디를 입력한 후 중복체크를 진행하세요", "error");
				// 패턴에 일치하지 않으면 페이지가 넘어가지 않음
				return false;
			}
			// idDuplChk 주소를 가진 서블릿에 요청하며, memberId 전달
			/*
			 location.href = '/idDuplChk?memberId=' + memberId.val();

			 서블릿에서 중복 체크후 회원가입 페이지로 이동하는 서블릿으로 요청하는 경우
			 현제 join.jsp 의 페이지가 다시 그려지며 입력했던 값들이 사라짐
			 */

			// 비동기 통신 : 서버에 요청을 보내고 응답이 올때까지 기다리지 않고 다음작음을 수행하다
			// ajax : 비동기 통신 기술 중 하나로 페이지 전환없이 서버와 데이터를 주고 받음
			//                                ^^^^^^^^^^^^
			$.ajax({
				url : "/idDuplChk",
				data : {
					"memberId" : memberId.val()
				},
				type : "GET",
				success : function(res) {
					if (res == 0) {
						// 중복된 아이디가 없음
						msg("알림", "사용가능한 아이디 입니다", "success");
						checkObj.idDuplChk = true;
					} else {
						msg("알림", "중복된 아이디가 존재합니다", "warning");
						checkObj.idDuplChk = false; // 아이디 중복체크 결과 저장
					}
				},
				error : function() {
					console.log('AJAX error has occured!!!');
				}
			});
		});

		const memberPw = $("#memberPw");
		const pwMessage = $("#pwMessage");

		const memberPwConfirm = $("#memberPwConfirm");
		memberPwConfirm.on("input", checkPw);

		function checkPw() {
			pwMessage.removeClass("valid invalid");

			if (memberPwConfirm.val() == memberPw.val()) {
				pwMessage.addClass("valid");
				pwMessage.html("비밀번호 일치");
				checkObj.memberPwConfirm = true;
			} else {
				pwMessage.addClass("invalid");
				pwMessage.html("비밀번호가 일치하지 않습니다.");
				checkObj.memberPwConfirm = false;
			}
		}

		memberPw.on("input", function() {
			pwMessage.removeClass("valid invalid");

			const regExp = /^(?=.*[0-9])(?=.*[!@#$])[a-zA-Z0-9!@#$]{6,30}$/;

			if (regExp.test($(this).val())) {
				checkObj.memberPw = true;

				if (memberPwConfirm.val().length < 1) {
					pwMessage.html("유효한 패턴");
					pwMessage.addClass("valid");
				} else {
					checkPw();
				}

			} else {
				pwMessage.html("비밀번호 형식이 유효하지 않습니다.");
				pwMessage.addClass("invalid");
				checkObj.memberPw = false;
			}
		});

		const memberName = $("#memberName");
		const nameMessage = $("#nameMessage");

		memberName.on("input", function() {
			nameMessage.removeClass("valid");
			nameMessage.removeClass("invalid");
			nameMessage.html("");

			const regExp = /^[가-힣]{2,10}$/;

			if (regExp.test($(this).val())) {
				nameMessage.html("");
				nameMessage.addClass("valid");
				checkObj.memberName = true;
			} else {
				nameMessage.html("이름은 한글 2~10 글자로 입력.");
				nameMessage.addClass("invalid");
				checkObj.memberName = false;
			}
		});

		const memberPhone = $("#memberPhone");
		const phoneMessage = $("#phoneMessage");

		memberPhone.on("input", function() {
			phoneMessage.removeClass("valid");
			phoneMessage.removeClass("invalid");

			const regExp = /^010-\d{3,4}-\d{4}$/;

			if (regExp.test($(this).val())) {
				phoneMessage.html("");
				phoneMessage.addClass("valid");
				checkObj.memberPhone = true;
			} else {
				phoneMessage.html("전화번호 형식이 유효하지 않습니다");
				phoneMessage.addClass("invalid");
				checkObj.memberPhone = false;
			}
		});

		function joinValidate() {
			let str = "";
			for ( let key in checkObj) {
				/*
				객체명.속성명 -> checkObj.key -> key 라는 속성명을 찾음 -> 이럼 안됨
				객체명[속성명] -> checkObj[key] -> value 를 반환
				 */

				/*
				각 입력값의 유효성 검사 결과를 저장하고 있는 객체의 현재값이 false 일때
				 */
				if (!checkObj[key]) {
					switch (key) {
					case "memberId":
						str = "아이디 형식";
						break;
					case "idDuplChk":
						str = "아이디 중복 체크를 진행하세요";
						break;
					case "memberPw":
						str = "비밀번호 형식";
						break;
					case "memberPwConfirm":
						str = "비밀번호 확인 형식";
						break;
					case "memberName":
						str = "이름 형식";
						break;
					case "memberPhone":
						str = "전화번호 형식";
						break;
					}

					if (key != "idDuplChk") {
						str += "이 유효하지 않습니다";
					}

					msg("회원가입 실패", str, "error");

					return false;
				}
			}
			return true;
		}
	</script>
</body>
</html>
