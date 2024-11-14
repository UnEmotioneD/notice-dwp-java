<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>adminPage.jsp file</title>
<style>
input[type=checkbox].chk+label {
	height: 24px;
}
</style>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content">
			<section class="section admin-wrap">
				<div class="page-title">회원 관리 페이지</div>
				<table class="tbl tbl-hover">
					<tr>
						<th style="width: 5%;">선택</th>
						<th style="width: 10%;">번호</th>
						<th style="width: 10%;">아이디</th>
						<th style="width: 10%;">이름</th>
						<th style="width: 10%;">전화번호</th>
						<th style="width: 10%;">이메일</th>
						<th style="width: 10%;">주소</th>
						<th style="width: 10%;">가입일</th>
						<th style="width: 10%;">회원등급</th>
						<th style="width: 10%;">등급변경</th>
					</tr>
					<c:forEach var="m" items="${memberList}">
						<tr>
							<td>
								<div class="input-wrap">
									<input type="checkbox" class="chk"> <label
										onclick="chkLabel(this)"></label>
								</div>
							</td>
							<td class="memberNo">${m.memberNo}</td>
							<td>${m.memberId}</td>
							<td>${m.memberName}</td>
							<td>${m.memberPhone}</td>
							<td>${m.memberEmail}</td>
							<td>${m.memberAddr}</td>
							<td>${m.enrollDate}</td>

							<td>
								<div class="select">
									<select id=memberLevelSelect onchange="memberLevelChange(this)">
										<c:choose>
											<%-- 2 일때는 정회원을 선택한 채로 --%>
											<c:when test="${m.memberLevel eq 2}">
												<option value="2" selected>정회원</option>
												<option value="3">준회원</option>
											</c:when>
											<%-- 3 일때는 준회원을 선택한 채로 --%>
											<c:when test="${m.memberLevel eq 3}">
												<option value="2">정회원</option>
												<option value="3" selected>준회원</option>
											</c:when>
										</c:choose>
									</select>
								</div>
							</td>

							<td>
								<button class="btn-primary sm" onclick="chgLevel(this)">등급변경</button>
							</td>
						</tr>
					</c:forEach>

					<tr>
						<td colspan="10">
							<button onclick="chgAllLevel()" class="btn-primary lg">선택
								회원 등급 변경</button>
						</td>
					</tr>
				</table>
			</section>
		</main>
	</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />

	<script>
	
		function refresh(){
			window.location.href = "/member/adminPage";
		}
	
		function chgLevel(obj) {
			let memberNo = $(obj).closest("tr").find(".memberNo").html();
			let memberLevel = $(obj).parent().prev().find(
					"select option:selected").val();

			/* 
			서블릿으로 요청
			- memberNo
			- memberLevel
			
			QUERY == UPDATE TBL_MEMBER SET MEMBER_LEVEL = ? WHERE MEMBER_NO = ?
			 */
			swal({
				title : "알림",
				text : "등급을 변경하시겠습니까?",
				icon : "warning",
				buttons : {
					cancel : {
						text : "취소",
						value : false,
						visible : true,
						closeModal : true,
					},
					confirm : {
						text : "변경",
						value : true,
						visible : true,
						closeModal : true,
					}
				}
			}).then(function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url : "/member/chgLevel",
						type : "GET",
						data : {
							"memberNo" : memberNo,
							"memberLevel" : memberLevel
						},
						success : function(res) {
							let title = "알림";
							let text = "";
							let icon = "";

							if (res > 0) {
								text = "등급이 변경되었습니다"
								icon = "success";
							} else {
								text = "등급 변경 중 오류가 발생하였습니다"
								icon = "error";
							}

							swal({
								title : title,
								text : text,
								icon : icon,
							});
							
							if(icon === "success"){
								refresh();
							}
						},
						error : function() {
							console.log("ajax 통신 오류");
						},
					});
				}
			});
		}

		function chkLabel(obj) {
			$(obj).prev().click();
		}

		
		function memberLevelChange(obj){
			$(obj).closest("tr").find(".chk").prop("checked", true);
		}

		function chgAllLevel() {
			// 체크된 체크박스
			let checkBoxes = $(".chk:checked"); // 클래스가 chk 인 태그 중에 checked 인 태그

			// console.log(checkBoxes);
			// length : 체크된 체크박스의 갯수

			if (checkBoxes.length < 1) {
				swal({
					title : "알림",
					text : "선택한 회원이 없습니다",
					icon : "warning",
				});
				return;
			}

			let memberNoArr = [];
			let memberLevelArr = [];

			$.each(checkBoxes, function(index, item) {
				memberNoArr
						.push($(item).parents("tr").find(".memberNo").html());

				memberLevelArr.push($(item).parents("tr").find(
						".select option:selected").val());
			});

			swal({
				title : "알림",
				text : "등급을 변경하시겠습니까?",
				icon : "warning",
				buttons : {
					cancel : {
						text : "취소",
						value : false,
						visible : true,
						closeModal : true
					},
					confirm : {
						text : "변경",
						value : true,
						visible : true,
						closeModal : true
					}
				}
			}).then(function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url : "/member/chgAllLevel",
						type : "GET",
						<%-- 배열 내부의 요소들을 "/" 로 나눔 --%>
						data : {
							"memberNoArr" : memberNoArr.join("/"), 
							"memberLevelArr" : memberLevelArr.join("/")
							},
						success : function(res) {
							let title = "알림";
							let text = "";
							let icon = "";

							if (res > 0) {
								text = "등급이 변경되었습니다";
								icon = "success";
							} else {
								text = "등급 변경 중 오류가 발생하였습니다";
								icon = "error";
							}

							swal({
								title : title,
								text : text,
								icon : icon,
							});
							
							if(icon === "success"){
								refresh();
							}
							
						},
						error : function() {
							console.log("foobar");
						}
					});
				}
			});
		}
	</script>

</body>
</html>
