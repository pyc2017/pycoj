<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<% String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/loading.css" rel="stylesheet" type="text/css">
    <jsp:include page="../boostrap.jsp"></jsp:include>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/question.js"></script>
    <script src="<%=path%>/resources/js/detail.js"></script>
    <title>${question.title}</title>
</head>
<body>
    <jsp:include page="../nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="container col-md-6 shadow">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#question" aria-controls="question" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span>&nbsp;Description</a></li>
                <li role="presentation"><a href="#result" aria-controls="result" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;Result</a></li>
                <li role="presentation"><a href="#comment" aria-controls="comment" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;Comment</a></li>
            </ul>
            <div role="tabpanel" class="tab-pane active" id="question">
                <h2>${question.title}</h2>
                <h3>Description</h3>
                <pre>${question.description}</pre>
                <h3>Input</h3>
                <pre>${question.input}</pre>
                <h3>Output</h3>
                <pre>${question.output}</pre>
                <h3>Hint</h3>
                <pre>${question.hint}</pre>
                <div style="margin-top: 30px;">
                    <button type="button" class="btn btn-lg" data-toggle="modal" data-target="#myModal">
                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                    </button>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade wrapp" id="result">
                <div class="states row"></div>
                <div id="state_refresh_button_block">
                    <button type="button" class="btn" id="state_refresh_button"><span class="glyphicon glyphicon-refresh" aria-hidden="true" onclick="refresh(${question.id})"></span></button>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade" id="comment">...</div>
        </div>
        <div class="col-md-4"></div>
    </div>
    <jsp:include page="../foot.jsp"></jsp:include>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">${question.title}</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="code">Code</label>
                        <textarea type="email" class="form-control" id="code" rows="8"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="lang">编译器</label>
                        <select class="form-control" id="lang">
                            <option value="0">gcc</option>
                            <option value="1">JDK1.8</option>
                            <option value="2">g++</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn submit" id="submit" onclick="submit(${question.id})">Submit</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>