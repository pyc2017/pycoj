<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="/resources/js/jquery-2.0.0.min.js"></script>
    <jsp:include page="boostrap.jsp"></jsp:include>
    <link href="/resources/css/index.css" rel="stylesheet" type="text/css">
    <script src="/resources/js/match.js"></script>
    <title>Match</title>
</head>
<body>
    <jsp:include page="nav.jsp"></jsp:include>
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8 container shadow">
            <a href="/match/exit">Quit This Match<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#matchQuestions" aria-controls="matchQuestions" role="tab" data-toggle="tab">Questions</a></li>
                <li role="presentation"><a href="#rank" aria-controls="rank" role="tab" data-toggle="tab">Rank</a></li>
            </ul>
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="matchQuestions"><table class="table" id="questionslist"></table><div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true"></div></div>
                <div role="tabpanel" class="tab-pane" id="rank">
                    <table class="table table-bordered" id="rankTable">
                        <thead><tr></tr></thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Solution</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="questionId">Question ID</label>
                        <select class="form-control" id="questionId">
                        </select>
                    </div>
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
                    <button type="button" class="btn btn-primary" onclick="submit()">Submit</button>
                </div>
            </div>
        </div>
    </div>
    <script>$(document).ready(function(){checkCreator();getQuestion();rank();$(".nav .dropdown").mouseenter(function () {$(this).addClass("open");$(this).children("a").attr("aria-expanded","true");});$(".nav .dropdown").mouseleave(function () {$(this).removeClass("open");$(this).children("a").attr("aria-expanded","false");});})</script>
</body>
</html>