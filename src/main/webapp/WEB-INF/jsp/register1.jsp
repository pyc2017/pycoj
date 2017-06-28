<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>邮箱注册</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="container col-md-8 form-horizontal">
            <div class="page-header" id="registerInfo">
                <h1>Welcome to Pyc OJ.</h1><p>Step 1 : Validate your Email!</p>
            </div>
            <div class="form" id="login-form">
                <h1><span class="glyphicon glyphicon-user" aria-hidden="true"></span></h1>
                <div class="form-group">
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="oj_email" placeholder="jane.doe@example.com">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <button class="btn btn-info form-control" type="button" id="register1_submit">Validate Your Email</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
<script type="text/javascript">
    $(document).ready(function () {
        $("#register1_submit").click(function(){
            $.ajax({
                url:"<%=path%>/register/send/",
                data:{
                    "email":$("#oj_email").val()
                },
                type:"POST",
                success:function () {
                    alert("请登陆邮箱，点击相应链接进行下一步操作");
                    window.location="<%=path%>/index/";
                }
            })
        })
    })
</script>
</body>
</html>
