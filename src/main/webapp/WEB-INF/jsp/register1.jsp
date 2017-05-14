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
            <p><span>@&nbsp;</span><input name="oj_email" type="text" placeholder="您的邮箱" id="oj_email"/> </p>
            <p><input name="submit" type="submit" value="提交" id="register1_submit"/> </p>
        </div>
    </div>
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
