<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<nav class="navbar navbar-default row">
    <div class="col-md-2"></div>
    <div class="container-fluid col-md-8">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<%=path%>/index/">OJ</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="<%=path%>/index/">首页</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/register_email/">注册</a></li>
                <li><a href="/login/">登陆</a></li>
            </ul>
        </div>
    </div>
    <div class="col-md-2"></div>
</nav>