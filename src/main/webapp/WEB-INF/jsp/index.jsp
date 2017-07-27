<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html lang="zh-CN">
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/index.js"></script>
    <title>Pyc OJ</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2 mycol-md-2"></div>
        <div class="container col-md-6 shadow">
            <table class="table" id="questionslist">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>title</th>
                        <th>submit</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <nav aria-label="Page navigation">
                <ul class="pagination">
                </ul>
            </nav>
        </div>
        <div class="col-md-1"></div>
        <div class="col-md-2 container shadow">
            <jsp:include page="coder_info.jsp"></jsp:include>
        </div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
<script>$(document).ready(function () {updateQuestion(1);getLoginUserInfo();})</script>
</body>
</html>
