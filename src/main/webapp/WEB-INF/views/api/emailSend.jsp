<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>emailSend.jsp</title>
</head>
<body>
	<div class="wrap">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<main class="content">
			<section class="section email-wrap">
				<div class="page-title">이메일 전송</div>
				<form action='/api/emailSend' method="post">
					<table class="tbl">
						<tr>
							<th style="width: 20%">제목</th>
							<td>
								<div class="input-item">
									<input type="text" name="emailTitle">
								</div>
							</td>
						</tr>

						<tr>
							<th>받는 사람</th>
							<td>
								<div class="input-item">
									<input type="text" name="receiver">
								</div>
							</td>
						</tr>

						<tr>
							<th>내용</th>
							<td>
								<div class="input-item">
									<textarea name="emailContent"></textarea>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<button class="btn-primary lg" type="submit">전송</button>
							</td>
						</tr>
					</table>
				</form>
			</section>
		</main>
		<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	</div>

</body>
</html>