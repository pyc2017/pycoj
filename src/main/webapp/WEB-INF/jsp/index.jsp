<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html lang="zh-CN">
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <link href="/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>Pyc OJ</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="container col-md-6">
            <table class="table table-striped" id="questionslist">
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
        <div class="col-md-4"></div>
    </div>
<script src="<%=path%>/resources/js/index.js"></script>
</body>
</html>
