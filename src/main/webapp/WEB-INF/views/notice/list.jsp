<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/views/notice/list.jsp</title>
<style>
.notice-list-wrap {
	width: 1200px;
	margin: 0 auto;
}

.list-content {
	height: 500px;
}

.list-header {
	padding: 15px 0px;
	text-align: right;
}
</style>
</head>
<body>
	<div class="warp">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content">
			<section class="section notice-list-wrap">
				<div class="page-title">${noticeCdNm}</div>

				<c:if test="${not empty loginMember}">
					<div class="list-header">
						<a class="btn-point" id="write-btn"
							href='/notice/writeFrm?noticeCd=${noticeCd}&noticeCdNm=${noticeCdNm}'>${noticeCdNm}
							작성</a>
						<a class="btn-point" id="write-btn"
							href='/notice/editorWriteFrm?noticeCd=${noticeCd}&noticeCdNm=${noticeCdNm}'>${noticeCdNm}
							작성(Editor)</a>
					</div>
				</c:if>

				<div class="list-content">

					<table class="tbl hover">

						<tr>
							<th style="width: 10%">번호</th>
							<th style="width: 45%">제목</th>
							<th style="width: 15%">작성자</th>
							<th style="width: 20%">작성일</th>
							<th style="width: 10%">조회수</th>
						</tr>

						<c:forEach var="notice" items="${noticeList}">
							<tr>
								<td>${notice.noticeNo}</td>
								<td><a href='/notice/view?noticeNo=${notice.noticeNo}'>${notice.noticeTitle}</a></td>
								<td>${notice.noticeWriter}</td>
								<td>${notice.noticeDate}</td>
								<td>${notice.readCount}</td>
							</tr>
						</c:forEach>
					</table>
				</div>

				<div id="pageNavi">${pageNavi}</div>
			</section>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" /></div>
</body>
</html>