<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chPw.jsp File</title>
<link rel="stylesheet" href="/resources/css/default.css" />
<style>
.wrap {
	min-width: 400px;
	min-height: 300px;
}

.section {
	display: flex;
	justify-content: center;
	align-items: center;
}

.pw-container {
	display: flex;
	aling-items: center;
}

.section {
	width: 400px;
	margin: 0 auto;
}

.pw-info-wrap {
	width: 80%;
}

.pw-btn {
	margin: 20px 0px;
	text-align: center;
}

.pw-btn>button {
	width: 85%;
}
</style>
</head>
<body>
	<div class="wrap">
		<main class="content pw-container">
			<section class="section">
				<div class="pw-info-wrap">

					<form id="pwChgForm" action="/member/pwChg" method="post">

						<input type="hidden" name="memberId"
							value="${loginMember.memberId}">

						<div class="input-wrap">
							<div class="input-title">
								<label for="memberPw">기존 비밀번호</label>
							</div>
							<div class="input-item">
								<input type="password" id="memberPw" name="memberPw">
							</div>
						</div>

						<div class="input-wrap">
							<div class="input-title">
								<label for="newMemberPw">새 비밀번호</label>
							</div>
							<div class="input-item">
								<input type="password" id="newMemberPw" name="newMemberPw"
									placeholder="영어, 숫자, 특수문자(!, @, #, $) 를 포함한 6~30 글자">
							</div>
						</div>

						<div class="input-wrap">
							<div class="input-title">
								<label for="newMemberPwRe">새 비밀번호 확인</label>
							</div>
							<div class="input-item">
								<input type="password" id="newMemberPwRe">
							</div>
							<p id="pwMessage" class="input-msg"></p>
						</div>

						<div class="pw-btn">
							<button type="button" onclick="pwChg()" class="btn-primary sm">변경하기</button>
						</div>

						<div class="pw-btn">
							<button type="button" onclick="closePop()"
								class="btn-secondary sm">닫기</button>
						</div>

					</form>
				</div>
			</section>
		</main>
	</div>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script src="/resources/js/sweetalert.min.js"></script>
	<script>
		const checkObj = {
			"memberPw" : false,
			"newMemberPw" : false,
			"newMemberPwRe" : false,
		};

		// 기존 비밀번호
		const memberPw = $("#memberPw");
		memberPw.on("input", function() {
			if (memberPw.val().length < 1) {
				// 입력값을 모두 지웠을때
				checkObj.memberPw = false;

			} else {
				// 기존 입력값이 존재할때
				checkObj.memberPw = true;
			}
		});

		// 새 비밀번호
		const newMemberPw = $("#newMemberPw");
		const pwMessage = $("#pwMessage"); // 새 비밀번호 및 확인 값에 대한 검증 겨로가 메세지를 보여줄 p tag

		// 새 비밀번호 확인값 runtime error 를 방지하기 위해 위에다가 선언
		const newMemberPwRe = $("#newMemberPwRe");

		newMemberPw.on("input", function() {
			pwMessage.removeClass("valid invalid");

			const regExp = /^(?=.*[0-9])(?=.*[!@#$])[a-zA-Z0-9!@#$]{6,30}$/;

			if (regExp.test(newMemberPw.val())) {
				// 새 비밀번호 입력값이 정규표현식 패턴에 만족할때
				checkObj.newMemberPw = true;

				// 새 비밀번호 확인값이 입력되어있는 상태일때
				if (newMemberPwRe.val().length > 0) {
					checkNewPwRe();
				} else {
					pwMessage.addClass("valid");
					pwMessage.html("유효한 패턴");
				}

			} else {
				checkObj.newMemberPw = false;
				pwMessage.addClass("invalid");
				pwMessage.html("비밀번호 형식이 유효하지 않습니다");
			}
		});

		newMemberPwRe.on("input", checkNewPwRe);

		function checkNewPwRe() {
			pwMessage.removeClass("valid invalid");

			if (newMemberPw.val() == newMemberPwRe.val()) {
				checkObj.newMemberPwRe = true;
				pwMessage.addClass("valid");
				pwMessage.html("비밀번호 일치");
			} else {
				checkObj.newMemberPwRe = false;
				pwMessage.addClass("invalid");
				pwMessage.html("비밀번호가 일치하지 않습니다");
			}
		}

		//변경하기 버튼 클릭시 submit 이전에 입력값 검증
		function pwChg() {
			let str = "";
			// key : 각 속성명
			for ( let key in checkObj) {

				// 값 속성에 대한 값이 false 인지
				if (!checkObj[key]) {
					switch (key) {
					case "memberPw":
						str = "기존 비밀번호를 입력하세요";
						break;
					case "newMemberPw":
						str = "새 비밀번호 형식이 유효하지 않습니다";
						break;
					case "newMemberPwRe":
						str = "비밀번호가 일치하지 않습니다";
						break;
					}
					swal({
						title : "알림",
						text : str,
						icon : "warning",
					});
					return false;
				}
			}

			swal({
				title : "변경",
				text : "비밀번호를 변경하시겠습니까?",
				icon : "warning",
				buttons : {
					cancel : {
						text : "취소",
						value : false,
						visible : true,
						closeModal : true
					},
					confirm : {
						text : "변경하기",
						value : true,
						visible : true,
						closeModal : true
					}
				}
			}).then(function(confirm) {
				if (confirm) {
					$("#pwChgForm").submit();
				}
			});
			return true;
		}

		// 팝업창이 생기면서 window 객체가 하나 생겼고 그걸 닫아주기
		function closePop() {
			self.close();
		}
	</script>
</body>
</html>
