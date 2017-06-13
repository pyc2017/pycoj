<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<% String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="../boostrap.jsp"></jsp:include>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>添加题目</title>
</head>
<body>
<jsp:include page="../nav.jsp"></jsp:include>
<div class="row">
    <div class="col-md-4"></div>
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
                    <label for="inputfile">Input File</label>
                    <input type="file" id="inputfile" name="inputfile">
                    <p class="help-block">Example block-level help text here.</p>
                </div>
                <div class="form-group">
                    <label for="outputfile">Output File</label>
                    <input type="file" id="outputfile" name="outputfile">
                    <p class="help-block">Example block-level help text here.</p>
                </div>
                <input type="hidden" name="accessable" value="true">
                <button type="submit" class="btn btn-default" id="submit">Submit</button>
            </form>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<script>
    $(document).ready(function () {
    })
</script>
</body>
</html>