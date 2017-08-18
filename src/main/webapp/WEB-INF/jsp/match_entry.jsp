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
        <div class="container col-md-8 form-horizontal">
            <div class="page-header" id="registerInfo">
                <h1>Match</h1>
            </div>
            <div class="form" id="login-form" style="padding-top:20px">
                <div class="form-group">
                    <div class="col-sm-10 has-feedback">
                        <input type="text" class="form-control" id="matchId" placeholder="Match ID" aria-describedby="matchIdStatus">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10 has-feedback">
                        <input type="text" class="form-control" id="matchPassword" placeholder="Password" aria-describedby="matchPasswordStatus">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <button class="btn btn-info form-control" type="button" onclick="getIn()">Enter</button>
                    </div>
                </div>
            </div>
            <div class="form" id="register-email-form"  style="padding-top:20px">
                <h4>New a match!</h4>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="matchName">Match Name</label>
                        <input type="text" class="form-control" id="matchName" placeholder="Name">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="matchPassword2">Match Password</label>
                        <input type="text" class="form-control" id="matchPassword2" placeholder="Match Password">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="startDate">Start Date</label>
                        <input type="date" class="form-control" id="startDate">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="startTime">Start</label>
                        <input type="time" class="form-control" id="startTime" placeholder="Match Password">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="endDate">End Date</label>
                        <input type="date" class="form-control" id="endDate">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="endTime">End</label>
                        <input type="time" class="form-control" id="endTime" placeholder="Match Password">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-10">
                        <button class="btn btn-info form-control" type="button" onclick="newMatch()">New!</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
    <jsp:include page="foot.jsp"></jsp:include>
    <script>$(document).ready(function(){$(".nav .dropdown").mouseenter(function () {$(this).addClass("open");$(this).children("a").attr("aria-expanded","true");});$(".nav .dropdown").mouseleave(function () {$(this).removeClass("open");$(this).children("a").attr("aria-expanded","false");});})</script>
</body>
</html>
