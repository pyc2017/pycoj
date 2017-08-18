var login_submit=function (path) {
    $.ajax({
        url: path+"/login/",
        data:{
            "username": $("#oj_username").val(),
            "password":$("#oj_password").val()
        },
        type:"POST",
        contentType:"application/x-www-form-urlencoded",
        success:function (data) {
            if (data=="success"){
                var reg=new RegExp("(^|&)original=([^&]*)(?=(&|$))");
                var target=window.location.search.substr(1);
                if (target.match(reg)==null||target.match(reg)==""){
                    window.location=path+"/index/";
                }else{
                    window.location=path+target.match(reg)[0].substring(9);
                }
            }else{
                errorFeedBack($("#oj_username"));
                errorFeedBack($("#oj_password"));
                $("#login_submit").css("background-color","#900b09");
            }
        }
    });
}
var register1_submit=function(path){
    $.ajax({
        url:path+"/register/send/",
        data:{
            "email":$("#oj_email").val()
        },
        type:"POST",
        success:function () {
            alert("请登陆邮箱，点击相应链接进行下一步操作");
            window.location=path+"/index/";
        }
    });
}
var errorFeedBack=function (e) {
    var parent=e.parent();
    parent.removeClass("has-success");
    parent.addClass("has-error");
    var children=[];
    children=parent.find("span");
    children.remove();
    parent.append("<span class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span><span id=\""+e.attr("id")+"Status\" class=\"sr-only\">(error)</span>")
}
var successFeedBack=function (e) {
    var parent=e.parent();
    parent.removeClass("has-error");
    parent.addClass("has-success");
    var children=[];
    children=parent.find("span");
    children.remove();
    parent.append("<span class=\"glyphicon glyphicon-ok form-control-feedback\" aria-hidden=\"true\"></span><span id=\""+e.attr("id")+"Status\" class=\"sr-only\">(success)</span>")
}