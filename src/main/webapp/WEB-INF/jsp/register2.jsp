<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <script src="<%=path%>/resources/js/login.js"></script>
    <script src="<%=path%>/resources/js/register2.js"></script>
    <title>邮箱注册</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="container col-md-8 form-horizontal">
            <div class="page-header" id="registerInfo">
                <h1>Welcome to Pyc OJ.</h1><p>Step 2 : Submit the information.</p>
            </div>
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
                    <div class="col-sm-10 has-feedback">
                        <input type="password" class="form-control" id="oj_password2" placeholder="Password" aria-describedby="oj_password2Status">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <button class="btn btn-info form-control" type="button" id="submit">Join</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
</body>
</html>