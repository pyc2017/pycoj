<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <title>添加题目</title>
</head>
<body>
<jsp:include page="nav.jsp"></jsp:include>
<div class="container">
    <div id="question" style="border: none;">
        <div class="form">
            <form method="post" action="/new/" enctype="multipart/form-data">
                <div>
                    <p><span>题目</span><input type="text" name="title"/></p>
                </div>
                <div class="textarea">
                    <p><span>题目描述</span><textarea name="description" class="shadow"></textarea></p>
                </div>
                <div class="textarea">
                    <p><span>输入示例</span><textarea type="text" name="input" class="shadow"></textarea></p>
                </div>
                <div class="file">
                    <p><span>输入用例</span><input type="file" name="inputfile"/></p>
                </div>
                <div class="textarea">
                    <p><span>输出示例</span><textarea type="text" name="output" class="shadow"></textarea></p>
                </div>
                <div class="file">
                    <p><span>输出用例</span><input type="file" name="outputfile"/></p>
                </div>
                <div class="textarea">
                    <p><span>hint</span><textarea type="text" name="hint" class="shadow"></textarea></p>
                </div>
                <div>
                    <input type="submit" value="提交"/>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>