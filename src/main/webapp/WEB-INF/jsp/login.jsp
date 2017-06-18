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
                <div class="col-sm-10">
                    <input type="email" class="form-control" id="oj_username" placeholder="Account">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-10">
                    <input type="password" class="form-control" id="oj_password" placeholder="Password">
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
                        $("#error").css("border-radius","1em");
                        $("#error").css("padding","5px");
                        $("#error").css("float","left");
                        $("#error").html("<span>账号或密码错误</span>");
                        var count=167;
                        setInterval(function(){
                            if (count<256){
                                count++;
                            }
                            $("#error").css("background-color","rgb("+count+",40,4)");
                        },10);
                    }
                }
            })
        })
    })
</script>
</body>
</html>