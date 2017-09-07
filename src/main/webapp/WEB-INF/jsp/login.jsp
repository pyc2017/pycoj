<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <script src="<%=path%>/resources/js/login.js"></script>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>登录</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="container col-md-8 form-horizontal">
            <div class="form" id="login-form">
                <h1><span class="glyphicon glyphicon-user" aria-hidden="true"></span></h1>
                <div class="form-group">
                    <div class="col-sm-10 has-feedback">
                        <input type="text" class="form-control" id="oj_username" placeholder="Account" aria-describedby="oj_usernameStatus">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10 has-feedback">
                        <input type="password" class="form-control" id="oj_password" placeholder="Password" aria-describedby="oj_passwordStatus">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <button class="btn btn-info form-control" type="button" id="login_submit" onclick="login_submit('<%=path%>')">Login</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
<script>
    $(document).ready(function () {
        $(".nav .dropdown").mouseenter(function () {$(this).addClass("open");$(this).children("a").attr("aria-expanded","true");});
        $(".nav .dropdown").mouseleave(function () {$(this).removeClass("open");$(this).children("a").attr("aria-expanded","false");});
    })
</script>
</body>
</html>