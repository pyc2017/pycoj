<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>邮箱注册</title>
</head>
<body>
<jsp:include page="nav.jsp"></jsp:include>
<div class="container">
    <div class="form">
        <p><span>@&nbsp;</span><input name="username" type="text" placeholder="您的账号" id="oj_username"/> </p>
        <p><span>&nbsp;P&nbsp;</span><input name="password" id="oj_password" type="password" placeholder="您的密码"/> </p>
        <p><input name="submit" type="submit" value="登录" id="login_submit"/> </p>
        <div id="error">
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#login_submit").click(function () {
            $.ajax({
                url:"<%=path%>/login/",
                data:{
                    "username": $("#oj_username").val(),
                    "password":$("#oj_password").val(),
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