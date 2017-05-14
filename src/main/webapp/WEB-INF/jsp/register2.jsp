<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <title>邮箱注册</title>
</head>
<body>
<jsp:include page="nav.jsp"></jsp:include>
<div class="container">
    <div class="header">
        <div class="header-description">
            <span>加入我们</span>
        </div>
    </div>
    <div class="form">
        <p><span>@&nbsp;</span><input name="oj_username" type="text" placeholder="您的账号" id="oj_username"/> </p>
        <p><span>&nbsp;P&nbsp;</span><input name="oj_password" id="oj_password" type="password" placeholder="您的密码"/> </p>
        <p><span>&nbsp;P&nbsp;</span><input name="oj_password_repeat" id="oj_password2" type="password" placeholder="重新输入您的密码"/> </p>
        <p><input name="submit" type="submit" value="提交" id="submit"/> </p>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var usernameFlag=false;
        var passwordFlag=false;
        $("#oj_username").blur(function () {
            $.ajax({
                url:"<%=path%>/register/checku/",
                data:{
                    "oj_username": $("#oj_username").val()
                },
                type:"POST",
                contentType:"application/x-www-form-urlencoded",
                success:function (data) {
                    if (data=="true"){
                        $("#oj_username").css("border-bottom","solid #45f63e");
                        $("#oj_username").css("border-right","solid #45f63e");
                        $("#oj_username").css("border-top","solid #45f63e");
                        $("#oj_username").css("border-left","solid #45f63e");
                        usernameFlag=true;
                    }else{
                        $("#oj_username").css("border-bottom","solid #ff0606");
                        $("#oj_username").css("border-right","solid #ff0606");
                        $("#oj_username").css("border-top","solid #ff0606");
                        $("#oj_username").css("border-left","solid #ff0606");
                        usernameFlag=false;
                    }
                }
            })
        })
        $("#oj_password2").blur(function () {
            if ($("#oj_password").val()==$("#oj_password2").val()){
                $("#oj_password").css("border-bottom","solid #45f63e");
                $("#oj_password").css("border-right","solid #45f63e");
                $("#oj_password").css("border-top","solid #45f63e");
                $("#oj_password").css("border-left","solid #45f63e");
                $("#oj_password2").css("border-bottom","solid #45f63e");
                $("#oj_password2").css("border-right","solid #45f63e");
                $("#oj_password2").css("border-top","solid #45f63e");
                $("#oj_password2").css("border-left","solid #45f63e");
                passwordFlag=true;
            }else{
                $("#oj_password").css("border-bottom","solid #ff0606");
                $("#oj_password").css("border-right","solid #ff0606");
                $("#oj_password").css("border-top","solid #ff0606");
                $("#oj_password").css("border-left","solid #ff0606");
                $("#oj_password2").css("border-bottom","solid #ff0606");
                $("#oj_password2").css("border-right","solid #ff0606");
                $("#oj_password2").css("border-top","solid #ff0606");
                $("#oj_password2").css("border-left","solid #ff0606");
                passwordFlag=false;
            }
        })
        $("#submit").click(function () {
            var reg=new RegExp("(^|&)email=([^&]*)(?=(&|$))");
            var email=window.location.search.substr(1).match(reg)[0].substring(6);
            if (usernameFlag&&passwordFlag){
                $.ajax({
                    url:"<%=path%>/register/",
                    data:{
                        "oj_username": $("#oj_username").val(),
                        "oj_password":$("#oj_password").val(),
                        "oj_email":email
                    },
                    type:"POST",
                    contentType:"application/x-www-form-urlencoded",
                    success:function (data) {
                        if (data=="success"){
                            alert("注册成功~!");
                            window.location="<%=path%>/index/";
                        }else{
                            alert("注册失败，请联系管理员");
                        }
                    }
                })
            }else{
                $("#submit").css("border-bottom","solid #ff0606");
                $("#submit").css("border-right","solid #ff0606");
                $("#submit").css("border-top","solid #ff0606");
                $("#submit").css("border-left","solid #ff0606");
            }
        })
    })
</script>
</body>
</html>