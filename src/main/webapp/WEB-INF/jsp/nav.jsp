<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="nav shadow">
    <div id="logoimg">
        <img src="/resources/img/logo.jpg"/>
    </div>
    <a class="nav-left" href="/index/">主页</a>
    <a class="nav-right" href="<%=request.getContextPath()%>/login/">登录</a>
    <a class="nav-right" href="<%=request.getContextPath()%>/register_email/">注册</a>
</div>