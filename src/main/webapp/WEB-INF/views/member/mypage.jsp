<%@page import="kr.or.iei.member.model.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>mypage.jsp File</title>
<style>
.mypage-container {
	display: flex;
	align-items: center;
}

.mypage-wrap {
	width: 800px;
	margin: 0 auto;
}

.my-info-wrap {
	width: 80%;
	margin: 0 auto;
}

.my-info-wrap .input-item input {
	padding: 0;
}

.mypage-btn {
	margin: 20px 0px;
	text-align: center;
}

.mypage-btn>button {
	width: 25%;
	margin: 10px 10px;
}
</style>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content mypage-container">
			<section class="section mypage-wrap">
				<div class="page-title">마이페이지</div>
				<div class="my-info-wrap">

					<form id="updateForm" action="/member/update" method="post">

						<input type="hidden" name="memberNo"
							value="${loginMember.memberNo}">

						<table class="tbl">
							<tr>
								<th width="30%">아이디</th>
								<td width="70%" class="left">${loginMember.memberId}</td>
							</tr>

							<tr>
								<th><label for="memberPw">비밀번호</label></th>
								<td class="left">
									<div class="input-wrap">
										<div class="input-item">
											<button type="button" onclick="chgPassword()"
												class="btn-primary sm">비밀번호 변경</button>
										</div>
									</div>
								</td>
							</tr>

							<tr>
								<th><label for="memberName">이름</label></th>
								<td class="left">
									<div class="input-wrap">
										<div class="input-item">
											<input type="text" id="memberName" name="memberName"
												value="${loginMember.memberName}" maxlength="10"
												placeholder="한글 2~10글자">
										</div>
									</div>
								</td>
							</tr>

							<tr>
								<th><label for="memberEmail">이메일</label></th>
								<td class="left">
									<div class="input-wrap">
										<div class="input-item">
											<input type="email" id="memberEmail" name="memberEmail"
												value="${loginMember.memberEmail}">
										</div>
									</div>
								</td>
							</tr>

							<tr>
								<th><label for="memberPhone">전화번호</label></th>
								<td class="left">
									<div class="input-wrap">
										<div class="input-item">
											<input type="text" id="memberPhone" name="memberPhone"
												value="${loginMember.memberPhone}" maxlength="13"
												placeholder="010-0000-0000">
										</div>
									</div>
								</td>
							</tr>

							<tr>
								<th><label for="memberAddr">주소</label></th>
								<td class="left">
									<div class="input-wrap">
										<div class="input-item">
											<input type="text" id="memberAddr" name="memberAddr"
												value="${loginMember.memberAddr}" maxlength="200">
										</div>
									</div>
								</td>
							</tr>

							<tr>
								<th>등급</th>
								<td class="left"><c:if
										test="${loginMember.memberLevel eq 1}"> 관리자</c:if> <c:if
										test="${loginMember.memberLevel eq 2}"> 정회원</c:if></td>
							</tr>

							<tr>
								<th>가입일</th>
								<td>${loginMember.enrollDate}</td>
							</tr>
						</table>

						<div class="mypage-btn">
							<button type="button" onclick="updateMember()"
								class="btn-primary lg">정보수정</button>

							<c:if test="${loginMember.memberLevel ne 1}">
								<button type="button" onclick="deleteMember()"
									class="btn-primary lg">회원탈퇴</button>
							</c:if>
							<c:if test="${loginMember.memberLevel eq 1}">
								<button type="button" onclick="moveAdminPage()"
									class="btn-point lg">관리자 페이지</button>
							</c:if>
						</div>

					</form>
				</div>
			</section>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	</div>
	<script>
		//정보 수정 버튼 클릭시 동작 함수
		function updateMember() {

			//회원가입 시 유효성 검사를 회원정보 수정에서도 적용해야 함
			const memberName = $('#memberName');
			const memberPhone = $('#memberPhone');

			const nameExp = /^[가-힣]{2,10}$/;
			const phoneExp = /^010-\d{3,4}-\d{4}$/;

			if (!nameExp.test(memberName.val())) {
				//알림창
				msg("알림", "이름은 한글 2~10글자 사이로 입력해주세요", "warning");
				return;
			}

			if (!phoneExp.test(memberPhone.val())) {
				//알림창
				msg("알림", "전화번호는 - 포함 13자리로 입력해주세요", "warning");
				return;
			}

			//정규표현식 패턴에 만족하는 경우 form 태그 submit
			swal({
				title : "알림",
				text : "회원 정보를 수정하시겠습니까?",
				icon : "warning",
				buttons : {
					cancel : {
						text : "취소",
						value : false,
						visible : true,
						closeModal : true
					},
					confirm : {
						text : "수정",
						value : true,
						visible : true,
						closeModal : true
					}
				}
			}).then(function(isConfirm) {
				//button에 나열된 버튼 클릭시 해당 객체의 value 속성 값을 매개변수로 전달해준다

				if (isConfirm) {
					//confirm == true == 수정을 눌렀을 때
					$('#updateForm').submit(); //action 속성에 지정된 서블릿으로 요청
				}
			});
		}

		function deleteMember() {
			swal({
				title : "회원탈퇴",
				text : "정말 회원탈퇴를 하시겠습니까?",
				icon : "warning",
				buttons : {
					cancel : {
						text : "취소",
						value : false,
						visible : true,
						closeModal : true
					},
					confirm : {
						text : "탈퇴",
						value : true,
						visible : true,
						closeModal : true
					}
				}
			}).then(function(isConfirm) {
				if (isConfirm) {
					$('#updateForm').attr('action', '/member/delete');
					$('#updateForm').submit();
				}
			});
		}

		function chgPassword() {
			/*
			open() 의 속성으로 전달할 수 있는 값
			
			width : 창의 가로 길이
			height : 창의 세로 길이
			top : 화면 상단에서 새창이 얼마나 떨어져 있을지
			left : 왼쪽에서 얼마나 떨어져 있을지
			 */

			let popupWidth = 500;
			let popupHeight = 400;

			// 화면 상단에서 얼마나 떨어져서 팝업이 보여질지
			let top = (window.innerHeight - popupHeight) / 2 + window.screenY;
			let left = (window.innerWidth - popupWidth) / 2 + window.screenX;

			window.open("/member/chgPwFrm", "chgPw", "width=" + popupWidth
					+ ", top=" + top + ", height=" + popupHeight + ", left="
					+ left);
		}

		//관리자 페이지 이동
	<%if (1 == ((Member) session.getAttribute("loginMember")).getMemberLevel()) {%>
		function moveAdminPage() {
			location.href = '/member/adminPage';
		}
	<%}%>
		
	</script>
</body>
</html>
