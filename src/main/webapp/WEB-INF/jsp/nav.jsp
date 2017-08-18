<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<nav class="navbar navbar-default row mynavbar">
    <div class="col-md-2"></div>
    <div class="container-fluid col-md-8">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="<%=path%>/index/"><strong>PYC</strong>OJ</a></li>
                <li><a href="/match">Match</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle nobackground-color" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-user" aria-hidden="true"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<%=path%>/login/">登录</a></li>
                        <li><a href="<%=path%>/logout/">退出登录</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="<%=path%>/uploadHeadImage">上传头像</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="col-md-2"></div>
</nav>
<div id="wrap">
    <div id="wrap-img"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></div>
    <h1>Welcome to Pyc OJ!</h1>
    <span>Coding</span>
</div>