<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>searchInfoFrm.jsp</title>
<style>
.wrap {
	min-width: 400px;
	min-height: 300px;
}

.srch-info-container {
	display: flex;
	align-items: center;
}

.srch-info-wrap {
	width: 80%;
}

.section {
	width: 400ps;
	margin: 0 auto;
}

.btn-wrap {
	display: flex;
	align-times: center;
	justify-content: center;
	gap: 10px;
}
</style>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="/resources/js/sweetalert.min.js"></script>
</head>
<body>
	<div class="wrap">
		<main class="content srch-info-container">
			<section class="section">
				<div class="srch-info-wrap">
					<c:if test="${gb eq 'id'}">
						<div class="page-title">아이디 찾기</div>
					</c:if>
					<c:if test="${gb eq 'pw'}">
						<div class="page-title">비밀번호 찾기</div>
					</c:if>

					<form id="srchInfoForm" action="/member/srchInfo" method="post">
						<div class="input-wrap">
							<div class="input-title">
								<label for="memberEmail">이메일 입력</label>
							</div>
							<div class="input-item">
								<input type="email" name="memberEmail" id="memberEmail">
							</div>
						</div>

						<c:if test="${gb eq 'pw'}">
							<div class="input-wrap">

								<div class="input-titlie">
									<label for="memberId">아이디 입력</label>

								</div>
								<div class="input-item">
									<input type="text" name="memberId" id="memberId">
								</div>
							</div>
						</c:if>
					</form>

					<div class="btn-wrap">
						<div>
							<button type="button" onclick="srchInfo('${gb}')"
								class="btn-primary md">찾기</button>
						</div>
						<div>
							<button type="button" onclick="closeFn('${gb}')"
								class="btn-secondary md">닫기</button>
						</div>
					</div>
				</div>
			</section>
		</main>
	</div>

	<script>
		function srchInfo(gb) {
			let memberEmail = $('#memberEmail');
			let link = ''; // 서블릿 url
			let param = {}; // 전송 데이터

			if (memberEmail.val().length < 1) {
				msg('알림', '이메일이 입력되지 않았습니다', 'warning');
				return;
			}

			param.memberEmail = memberEmail.val();

			if (gb == 'id') {
				link = '/member/srchInfo';

			} else if (gb == 'pw') {
				link = '/member/srchInfoPw';

				let memberId = $('#memberId');

				if (memberId.val().length < 1) {
					msg('알림', '아이디가 입력되지 않았습니다', 'warning');
					return;
				}

				param.memberId = memberId.val();

			} else {
				msg('알림', '구분값이 입력되지 않았습니다', 'warning');
				return;
			}

			$.ajax({
				url : link,
				data : param,
				type : "GET",
				success : function(res) {

				},
				error : function() {
					console.log("ajax 통신 오류");
				}
			});
		}

		function closeFn() {
			self.close();
		}

		function msg(title, text, icon) {
			swal({
				title : title,
				text : text,
				icon : icon,
			});
		}
	</script>

</body>
</html>