<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
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
                        successFeedBack($("#oj_username"));
                        usernameFlag=true;
                    }else{
                        errorFeedBack($("#oj_username"));
                        usernameFlag=false;
                    }
                }
            })
        })
        $("#oj_password2").blur(function () {
            if ($("#oj_password").val()==$("#oj_password2").val()){
                successFeedBack($("#oj_password"));
                successFeedBack($("#oj_password2"));
                passwordFlag=true;
            }else{
                errorFeedBack($("#oj_password"));
                errorFeedBack($("#oj_password2"));
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
                        "username": $("#oj_username").val(),
                        "password":$("#oj_password").val(),
                        "email":email
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
            }
        })
    })
    var successFeedBack=function (e) {
        var parent=e.parent();
        parent.removeClass("has-error");
        parent.addClass("has-success");
        var children=[];
        children=parent.find("span");
        children.remove();
        parent.append("<span class=\"glyphicon glyphicon-ok form-control-feedback\" aria-hidden=\"true\"></span><span id=\""+e.attr("id")+"Status\" class=\"sr-only\">(success)</span>")
    }
    var errorFeedBack=function (e) {
        var parent=e.parent();
        parent.removeClass("has-success");
        parent.addClass("has-error");
        var children=[];
        children=parent.find("span");
        children.remove();
        parent.append("<span class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span><span id=\""+e.attr("id")+"Status\" class=\"sr-only\">(error)</span>")
    }
</script>
</body>
</html>