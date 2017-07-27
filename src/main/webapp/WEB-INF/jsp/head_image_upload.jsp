<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath(); %>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <script src="<%=path%>/resources/js/fileinput.min.js"></script>
    <link href="<%=path%>/resources/css/fileinput.min.css" type="text/css" rel="stylesheet">
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/index.js"></script>
    <title>上传图像</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2 mycol-md-2"></div>
        <div class="col-md-8 container shadow">
            <form action="/uploadHeadImage" method="post" enctype="multipart/form-data">
                <label class="control-label">选择图片</label>
                <input id="input-1" type="file" class="file" name="headImage"/>
            </form>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
<script>
    $(document).ready(function () {
        $("#input-1").fileinput({
            language: 'zh',
            uploadUrl: '/uploadHeadImage',
            allowedFileExtensions: ['jpg','png'],
            showUpload: false,
        })
    })
</script>
</body>
</html>
