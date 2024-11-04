<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="/resources/css/default.css" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons"
	type="text/css" />
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="/resources/js/sweetalert.min.js"></script>

<header class="header">
	<div>
		<div class="Logo">
			<a href="/index.jsp">UnEmotioneD</a>
		</div>
		<nav class="nav">
			<ul>
				<%-- Listener 에서 조회한 메뉴 종류 --%>
				<c:forEach var="noticeType" items="${noticeTypeList}">
					<li><a
						href="/notice/list?reqPage=1&noticeCd=${noticeType.noticeCd}&noticeCdNm=${noticeType.noticeCdNm}">${noticeType.noticeCdNm}</a></li>
				</c:forEach>

				<li><a href='api/naverMaps'>API</a>
					<ul class="sub-menu">
						<li><a href='/api/naverMaps'>지도</a>
						<li><a href='/api/emailSendFrm'>이메일</a>
					</ul></li>

			</ul>
		</nav>
		<ul class="user-menu">
			<c:choose>
				<c:when test="${empty sessionScope.loginMember}">
					<li><a href="/member/loginFrm">로그인</a></li>
					<li><a href="/member/joinFrm">회원가입</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="/member/mypage">${loginMember.memberName}님</a></li>
					<li><a href="/member/logout">로그아웃</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>

	<script>
		function msg(title, text, icon) {
			swal({
				title : title,
				text : text,
				icon : icon
			});
		}
	</script>

</header>