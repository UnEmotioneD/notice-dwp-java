<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index.jsp File</title>
<style>
.section {
	margin-bottoma: 20px;
}

.list-header {
	text-align: right;
	margin-bottom: 10px;
}

.list-header>a:hover {
	text-decoration: underline;
}
</style>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content">
			<c:forEach var="notice" items="${noticeTypeList}">
				<section class="section type${notice.noticeCd}">
					<div class="page-title" style="text-align: left;">${notice.noticeCdNm}
						<div class="list-header">
							<a
								href='/notice/list?reqPage=1&noticeCd=${notice.noticeCd}&noticeCdNm=${notice.noticeCdNm}'>더보기...</a>
						</div>
						<div class="list-content">
							<table class="tbl hover">
								<tr class="th">
									<th style="width: 10%;">번호</th>
									<th style="width: 50%;">제목</th>
									<th style="width: 20%;">작성자</th>
									<th style="width: 20%;">작성일</th>
								</tr>
							</table>
						</div>
					</div>
				</section>
			</c:forEach>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	</div>

	<script>
		// 게시글 종류별 리스트 조회
		function noticeList() {
			$.ajax({
				url : "/notice/index",
				type : "GET",
				dataType : "json", // 서블릿에서 응답해주는 데이터의 형식
				success : function(res) {
					$(res).each(
							function(index, item) {
								let html = '';
								html += "<tr>";
								html += "<td>" + item.noticeCd + "</td>";
								html += "<td><a href='/notice/view?noticeNo="
										+ item.noticeNo + "'>"
										+ item.noticeTitle + "</a></td>";
								html += "<td>" + item.noticeWriter + "</td>";
								html += "<td>" + item.noticeDate + "</td>";
								html += "</tr>";

								$('.section.type' + item.noticeCd).find('.tbl')
										.append(html);
							});
				},
				error : function() {
					console.log("ajax 통신 오류");
				}
			});
		}

		setInterval(function() {
			noticeList();
		}, 1000 * 60 * 10); // 10 minute interval

		// 처음에도 동작할 수 있도록 페이지 로드 되면 실행
		$(function() {
			noticeList();
		});
	</script>

</body>
</html>
