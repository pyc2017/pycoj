<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
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
                        <button class="btn btn-info form-control" type="button" id="login_submit">Login</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
<script type="text/javascript">
    $(document).ready(function () {
        $("#login_submit").click(function () {
            $.ajax({
                url:"<%=path%>/login/",
                data:{
                    "username": $("#oj_username").val(),
                    "password":$("#oj_password").val()
                },
                type:"POST",
                contentType:"application/x-www-form-urlencoded",
                success:function (data) {
                    if (data=="success"){
                        window.location="<%=path%>/index/";
                    }else{
                        errorFeedBack($("#oj_username"));
                        errorFeedBack($("#oj_password"));
                    }
                }
            })
        })
        var errorFeedBack=function (e) {
            var parent=e.parent();
            parent.removeClass("has-success");
            parent.addClass("has-error");
            var children=[];
            children=parent.find("span");
            children.remove();
            parent.append("<span class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span><span id=\""+e.attr("id")+"Status\" class=\"sr-only\">(error)</span>")
        }
    })
</script>
</body>
</html>