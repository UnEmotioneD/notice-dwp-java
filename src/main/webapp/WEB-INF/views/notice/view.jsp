<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>view.jsp</title>
<style>
.notice-view-wrap {
	width: 1200px;
	margin: 0 auto;
}

.noticeContent {
	min-height: 300px;
}

.comment-write {
	overflow: hidden;
}

.comment-write textarea[name=commentContent] {
	width: 1000px;
	height: 100px;
	margin-right: 10px;
}

.comment-write li {
	float: left;
}

.inputCommentBox {
	maring-botton: 20px;
}

.comment-write button {
	width: 150px;
	height: 130px;
	font-size: 20px;
}

.commentBox ul {
	margin-bottom: 150px;
	border-bottom: solid 1px var(--gray5);
}
</style>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content">
			<section class="section notice-view-wrap">
				<div class="page-title">${notice.noticeCdNm}</div>
				<table class="tbl notice-view">

					<tr>
						<th colspan="6">${notice.noticeTitle}</th>
					</tr>
					<tr>
						<th style="width: 20%">작성자</th>
						<td style="width: 20%">${notice.noticeWriter}</td>
						<th style="width: 15%">작성일</th>
						<td style="width: 15%">${notice.noticeDate}</td>
						<th style="width: 15%">조회수</th>
						<td style="width: 15%">${notice.readCount}</td>
					</tr>

					<tr>
						<th>첨부파일</th>
						<td colspan="5"><c:forEach var="file"
								items="${notice.fileList}">
								<a
									href="javascript:fileDown('${file.fileName}', '${file.filePath}')">${file.fileName}</a>
							</c:forEach></td>
					</tr>

					<tr>
						<td class="left" colspan="6">
							<div class="noticeContent">${notice.noticeContent}</div>
						</td>
					</tr>

					<c:if
						test="${not empty loginMember and loginMember.memberId eq notice.noticeWriter}">
						<tr>
							<td colspan="6"><a
								href='/notice/updateFrm?noticeNo=${notice.noticeNo}'
								class="btn-primary">수정</a>
								<button class="btn-secondary"
									onclick="deleteNotice('${notice.noticeNo}')">삭제</button>
						</tr>
					</c:if>
				</table>

				<c:if test="${not empty loginMember}">
					<div class="inputCommentBox">
						<form name="insertComment" action="/notice/insertComment"
							method="post">
							<%-- 현재 게시글 번호 --%>
							<input type="hidden" name="commentRef" value="${notice.noticeNo}">
							<%-- 댓글 작성자는 현재 로그인한 회원 --%>
							<input type="hidden" name="commentWriter"
								value="${loginMember.memberId}">

							<ul class="comment-write">
								<li>
									<div class="input-item">
										<textarea name="commentContent"></textarea>
									</div>
								</li>

								<li>
									<button type="submit" class="btn-primary">등록</button>
								</li>
							</ul>

						</form>
					</div>
				</c:if>
				<div class="commentBox">
					<c:forEach var="comment" items="${notice.commentList}">
						<ul class="posting-comment">
							<li><span class="material-icons">account_box</span></li>
							<li>
								<p class="comment-info">
									<span>${comment.commentWriter}</span> <span>${comment.commentDate}</span>

									<%-- 로그인한 회원 아이디 == 현재 댓글을 작성한 아이디 --%>
									<c:if
										test="${not empty loginMember and loginMember.memberId eq comment.commentWriter}">
										<a href='javascript:vod(0)'
											onclick="mdfComment(this, '${comment.commentNo}');">수정</a>
										<a href='javascript:void(0)'
											onclick="deleteComment('${comment.commentNo}');">삭제</a>
									</c:if>
								</p>

								<p class="comment-content">${comment.commentContent}</p>

								<div class="input-item">
									<textarea name="commentContent">${comment.commentContent}</textarea>
								</div>

							</li>
						</ul>
					</c:forEach>
				</div>

			</section>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	</div>

	<script>
		function fileDown(fileName, filePath) {
			location.href = '/notice/fileDown?fileName=' + fileName
					+ '&filePath=' + filePath;
		}

		function deleteNotice() {
			window.alert("Create deleteNotice function()");
		}

		function deleteComment(commentNo) {
			swal({
				title : "삭제",
				text : "댓글을 삭제 하세겠습니까?",
				icon : "warning",
				buttons : {
					cancle : {
						text : "취소",
						value : false,
						visible : true,
						closeModal : true,
					},
					confirm : {
						text : "삭제",
						value : true,
						visible : true,
						closeModal : true,
					}
				}
			}).then(
					function(isConfirm) {
						if (isConfirm) {
							// 서블릿에서 등록한 notice 의 noticeNo (댓글 삭제 후 상세보기 이동 시 필요)
							let noticeNo = '${notice.noticeNo}';
							location.href = 'notice/deleteComment?noticeNo='
									+ noticeNo + '&commentNo=' + commentNo;
						}
					});
		}

		function mdfComment(obj, commentNo) {
			// obj : 수정 a 링크 요소 객체
			// commentNo : 댓글 번호
			let noticeNo = '${notice.noticeNo}'; // 수정 완료 후 상세보기 이동 시 필요

			// 기존 댓글 출력 요소 숨김 처리, 입력 수정 입력란 보여주기
			let commentContentLi = $(obj).parents('li');
			$(commentContentLi).find('div.input-item').show();
			$(commentContentLi).find('p.comment-content').hide();

			// 기존 '수정' -> '수정완료'
			$(obj).text('수정완료');
			$(obj).attr('onclick',
					'mdfCommentComplete(this, "' + commentNo + '")');

			// 기존 '삭제' -> '수정취소'
			$(obj).next().text('수정취소');
			$(obj).next().attr('onclick',
					'mdfCommentCancel(this, "' + commentNo + '")');

		}

		function mdfCommentComplete(obj, commentNo) {
			// obj : 수정완료 a 링크 요소 객체
			let form = $('<form>');
			form.attr('action', '/notice/updateComment');
			form.attr('method', 'post');

			let noticeNo = '${notice.noticeNo}'; // 수정 완료 후 다시 상세보기로 이동시 필요
			let noticeNoEl = $('<input>');
			noticeNoEl.attr('type', 'text');
			noticeNoEl.attr('name', 'noticeNo');
			noticeNoEl.attr('value', 'noticeNo');

			// 댓글 번호
			let commentNoEl = $('<input>');
			commentNoEl.attr('type', 'text');
			commentNoEl.attr('name', 'commentNo');
			commentNoEl.attr('value', 'commentNo');

			// 수정된 댓글 내용
			let commentContentEl = $(obj).parents('li').find('div.input-item');

			// 폼 태그 하위로 전송 파라미터 값 삽입
			form.append(noticeNoEl).append(commentNoEl)
					.append(commentContentEl);
			$('.body').append(form);
			form.submit();
		}

		function mdfCommentCancel(obj, commentNo) {
			// obj : 수정취소 a 링크 요소 객체
			let commentContentLi = $(obj).parents('li');

			// 수정할 수 있는 입력란 숨기기, 기존 댓글 출력 요소 보여주기
			$(commentContentLi).find('div.input-item').hide();
			$(commentContentLi).find('p.comment-content').show();

			// 수정취소 -> 삭제
			$(obj).text('삭제');
			$(obj).attr('onclick', 'delComment("' + commentNo + '")');

			// 수정완료 -> 수정
			$(obj).prev().text('수정');
			$(obj).prev().attr('onclick',
					'mdfComment(this, "' + commentNo + '")');
		}
	</script>

</body>
</html>