<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%
    String path=request.getContextPath();
%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/loading.css" rel="stylesheet" type="text/css">
    <jsp:include page="../boostrap.jsp"></jsp:include>
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/detail.js"></script>
    <title>${question.title}</title>
</head>
<body>
    <jsp:include page="../nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="container col-md-6">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#question" aria-controls="question" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span>&nbsp;Description</a></li>
                <li role="presentation"><a href="#result" aria-controls="result" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;Result</a></li>
                <li role="presentation"><a href="#history" aria-controls="history" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>&nbsp;Submit History</a></li>
                <li role="presentation"><a href="#comment" aria-controls="comment" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;Comment</a></li>
            </ul>
            <div role="tabpanel" class="tab-pane shadow active" id="question">
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
                    <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                    </button>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade" id="history">...</div>
            <div role="tabpanel" class="tab-pane fade wrapp" id="result"></div>
            <div role="tabpanel" class="tab-pane fade" id="comment">...</div>
        </div>
        <div class="col-md-4"></div>
    </div>
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
                            <option value="0">gcc++</option>
                            <option value="1">JDK1.8</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary" id="submit">Submit</button>
                </div>
            </div>
        </div>
    </div>
<script>
    $(document).ready(function () {
        $(".nav-tabs a").click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })
        $("#submit").click(function () {
            $(".close").click();
            $("a[href='#result']").click();
           $.ajax({
                url: "/submit/${question.id}",
                type: "post",
                data:{
                    "code": $("#code").val(),
                    "lang": $("#lang").val()
                },
                success: function (data) {
                    $("#result").empty();
                    switch (data["state"]){
                        case 0:
                            $("#result").append("<div class=\"alert alert-success\" role=\"alert\"><p>Accepted</p></div>");
                            break;
                        case 1:
                            $("#result").append("<div class=\"alert alert-danger\" role=\"alert\"><p>编译错误</p></div>");
                            $("#result div").append("<p>"+data["info"]+"</p>");
                            break;
                    }
                },
                beforeSend: function () {
                    $("#result").empty();
                    $("#result").append("<div class=\"col-md-3\"></div><div class=\"load-3 col-md-3\"></div>");
                    for (var i=1;i<=8;i++) {
                        $(".load-3").append("<div class=\"k-line2 k-line12-"+i+"\"></div>");
                    }
                }
            })
        })
    })
</script>
</body>
</html>