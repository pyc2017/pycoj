<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>Pyc OJ</title>
</head>
<body>
<jsp:include page="nav.jsp"></jsp:include>
<div class="container">
    <div id="questions">
        <ul>
        </ul>
    </div>
    <div id="pagenumbers">
        <div>
        </div>
    </div>
</div>
<script src="<%=path%>/resources/js/index.js"></script>
</body>
</html>
