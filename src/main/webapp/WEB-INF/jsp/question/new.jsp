<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<% String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="../boostrap.jsp"></jsp:include>
    <link type="text/css" rel="stylesheet" href="/resources/css/fileinput.min.css">
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="/resources/js/fileinput.min.js"></script>
    <script src="/resources/js/fileinput_locale_zh.js"></script>
    <title>添加题目</title>
</head>
<body>
<jsp:include page="../nav.jsp"></jsp:include>
<div class="row">
    <div class="col-md-4">
        <div id="example_zip_file">
            <h3>An Example of The Zip File</h3>
            <p>After the compression,there should not be any folders in the zip file.</p>
            <img src="<%=path%>/resources/img/example.png">
        </div>
    </div>
    <div class="container col-md-6">
        <div id="question" style="border: none;">
            <form class="form" action="<%=path%>/new/" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" class="form-control" id="title" placeholder="Title" name="title">
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea type="text" class="form-control" name="description" id="description" placeholder="Given two integers,you need to calculate the sum of these two integers"></textarea>
                </div>
                <div class="form-group">
                    <label for="input">Input</label>
                    <textarea type="text" class="form-control" name="input" id="input" placeholder="An example of input..." rows="6"></textarea>
                </div>
                <div class="form-group">
                    <label for="output">Output</label>
                    <textarea type="text" class="form-control" name="output" id="output" placeholder="An example of output..." rows="2"></textarea>
                </div>
                <div class="form-group">
                    <label for="hint">Hint</label>
                    <textarea type="text" class="form-control" name="hint" id="hint"></textarea>
                </div>
                <div class="form-group">
                    <label for="zip">Zip File</label>
                    <input type="file" id="zip" name="zip" class="projectfile">
                    <p class="help-block">Example block-level help text here.</p>
                </div>
                <button type="submit" class="btn btn-default" id="submit">Submit</button>
            </form>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<script>
    $(document).ready(function () {
        var s;
        if ((s=window.location.search)!==""){
            var reg=new RegExp("(^|&)m=([^&]*)(?=(&|$))");
            var id=s.substring(1).match(reg)[0].substring(2);
            $("form").attr("action","/newMatchQuestion");
            $("form").append('<input type="hidden" name="matchId" value="'+id+'">');
        }
        var projectfileoptions = {
            showUpload : false,
            showRemove : false,
            language : 'zh',
            allowedPreviewTypes : [ 'image' ],
            allowedFileExtensions : [ 'zip'],
            maxFileSize : 2000
        };
        // 文件上传框
        $('input[class="projectfile"]').each(function() {
            var imageurl = $(this).attr("value");

            if (imageurl) {
                var op = $.extend({
                    initialPreview : [ // 预览图片的设置
                        "<img src='" + imageurl + "' class='file-preview-image'>", ]
                }, projectfileoptions);

                $(this).fileinput(op);
            } else {
                $(this).fileinput(projectfileoptions);
            }
        });
    })
</script>
</body>
</html>