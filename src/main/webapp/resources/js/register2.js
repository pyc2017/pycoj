$(document).ready(function () {
    var usernameFlag=false;
    var passwordFlag=false;
    $("#oj_username").blur(function () {
        $.ajax({
            url: "/register/checku/",
            data:{
                "oj_username": $("#oj_username").val()
            },
            type:"POST",
            contentType:"application/x-www-form-urlencoded",
            success:function (data) {
                if (data=="true"){
                    successFeedBack($("#oj_username"));
                    usernameFlag=true;
                }else{
                    errorFeedBack($("#oj_username"));
                    usernameFlag=false;
                }
            }
        })
    })
    $("#oj_password2").blur(function () {
        if ($("#oj_password").val()==$("#oj_password2").val()){
            successFeedBack($("#oj_password"));
            successFeedBack($("#oj_password2"));
            passwordFlag=true;
        }else{
            errorFeedBack($("#oj_password"));
            errorFeedBack($("#oj_password2"));
            passwordFlag=false;
        }
    })
    $("#submit").click(function () {
        var reg=new RegExp("(^|&)email=([^&]*)(?=(&|$))");
        var email=window.location.search.substr(1).match(reg)[0].substring(6);
        if (usernameFlag&&passwordFlag){
            $.ajax({
                url:"/register/",
                data:{
                    "username": $("#oj_username").val(),
                    "password":$("#oj_password").val(),
                    "email":email
                },
                type:"POST",
                contentType:"application/x-www-form-urlencoded",
                success:function (data) {
                    if (data=="success"){
                        alert("注册成功~!");
                        window.location="/index/";
                    }else{
                        alert("注册失败，请联系管理员");
                    }
                }
            })
        }else{
        }
    })
})