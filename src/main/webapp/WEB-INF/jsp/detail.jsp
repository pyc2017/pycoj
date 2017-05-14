<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/detail.js"></script>
    <title>${question.title}</title>
</head>
<body>
<jsp:include page="nav.jsp"></jsp:include>
<div class="container">
    <div id="question" class="shadow">
        <div>
            <div class="question-title">
                ${question.title}
            </div>
            <hr/>
            <div class="question-description">
                <p>题目描述</p>
                <p>${question.description}</p>
            </div>
            <hr/>
            <div class="question-input">
                <p>input</p>
                <p>${question.input}</p>
            </div>
            <hr/>
            <div class="question-output">
                <p>output</p>
                <p>${question.output}</p>
            </div>
            <hr/>
            <div class="question-hint">
                <p>hint</p>
                <p>
                    ${question.hint}
                </p>
            </div>
        </div>
    </div>
    <div id="question-submit-button" class="shadow">
        <input type="image" src="<%=path%>/resources/img/pencil.jpg" class="pencil"/>
    </div>
    <div class="modal">
    </div>
    <div class="modal-data shadow">
        <div class="modal-close">
            <button>
                x
            </button>
        </div>
        <div class="form">
            <form method="post" action="/submit/${question.id}/">
                <div class="textarea">
                    <p><span>code</span><textarea type="text" name="code"></textarea></p>
                </div>
                <div class="select">
                    <select name="lang">
                        <option value="0">gcc</option>
                        <option value="1">java 1.8</option>
                    </select>
                </div>
                <div>
                    <p><input type="image" src="<%=path%>/resources/img/pencil.jpg" class="pencil"/></p>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>