<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<div class="user-head-div"><img src="<%=path%>/resources/img/head/default.jpg">
</div>
<div class="user-infomation-div list-group">
    <a class="list-group-item active"><span class="badge" id="user-information-div-nickname"></span>昵称</a>
    <a class="list-group-item"><span class="badge" id="acAmount">0</span>AC</a>
</div>