<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>updateFrm.jsp</title>
<style>
.delBtn:hover {
	sursor: pointer;
}

.delBtn {
	display: inline-block;
	height: 30px;
	vertical-align: middle;
}
</style>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />

		<main class="content">
			<section class="section notice-update-wrap">
				<div class="page-title">${notice.noticeCdNm}수정</div>
				<form action="/notice/update" method="post"
					enctype="mulippart/form-data">
					<input type="hidden" name="noticeNo" value="${notice.noticeNo}">
					<table class="tbl">
						<tr>
							<th style="width: 15%;">제목</th>
							<td>
								<div class="input-item">
									<input type="text" name="noticeTitle"
										value="${notice.noticeTitle}">
								</div>
							</td>
						</tr>

						<tr>
							<th>첨부파일</th>
							<td>
								<div class="file-wrap left">
									<c:forEach var="file" items="${notice.fileList}">
										<div class="files">
											<span class="delFileName">${file.fileName}</span> <span
												class="material-icons delNtn"
												onclick="delFile(this, '${file.fileNo}')">remove_circle</span>

										</div>
									</c:forEach>
								</div>
							</td>
						</tr>

						<tr>
							<th>추가파일</th>
							<td><input type="file" name="addFile"></td>
						</tr>

						<tr>
							<th>내용</th>
							<td class="left">
								<div class="input-item">
									<textarea name="noticeContent">${notice.noticeContent}</textarea>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<button class="btn-primary lg">수정</button>
							</td>
						</tr>

					</table>
				</form>
			</section>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	</div>


	<%-- summerNote 사용을 위한 라이브러리 --%>
	<script src="/resources/summernote/summernote-lite.js"></script>
	<script src="/resources/summernote/lang/summernote-ko-KR.js"></script>
	<link rel="stylesheet" href="/resources/summernote/summernote-lite.css">

	<script>
		//파일 삭제 아이콘 (-) 클릭시 동작 함수
		function delFile(obj, fileNo) {
			swal({

				title : "삭제",
				text : "첨부파일을 삭제하시겠습니까?",
				icon : "warning",
				buttons : {
					cancel : {
						text : "취소",
						value : false,
						visible : true,
						colseModal : true
					},
					confirm : {
						text : "삭제",
						value : true,
						visible : true,
						colseModal : true
					}
				}
			}).then(function(isConfirm) {
				if (isConfirm) {
					// 삭제 클릭 시 실시간으로 삭제처리하지 않고 수정 버튼 클릭시 삭제될 수 있도록 form 태그 내부에 hidden 으로 추가
					let inputEl = $('<input>');
					inputlEl.attr('type', 'hidden');
					inputEl.attr('name', 'delFileNo');
					inputlEl.attr('value', fileNo);

					$(obj).parent().remove(); // 화면에서 사라지도록
					$('form').parent(inputEl); // 첫번째 자식으로 추가
				}
			});
		}

		$('textarea[name=noticeContent]').summernote({
			height : 500,
			width : 1000,
			lang : "ko-KR",
			disableResize : true,
			disableResizeEditor : true,
			resize : true
		});
	</script>
</body>
</html>