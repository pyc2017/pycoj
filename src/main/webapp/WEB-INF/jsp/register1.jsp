<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path=request.getContextPath();
%>
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
        <div class="container col-md-8 form-inline">
            <div class="form-group">
                <label for="oj_email">Email</label>
                <input type="email" class="form-control" id="oj_email" placeholder="jane.doe@example.com">
            </div>
        </div>
        <div class="col-md-2"></div>
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
