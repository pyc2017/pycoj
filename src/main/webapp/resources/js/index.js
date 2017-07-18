$(document).ready(function () {
    var scrollLength=0;
    $(this).scroll(function () {//导航栏
        if ($(this).scrollTop()>scrollLength){
            $(".navbar").fadeOut();
        }else {
            $(".navbar").fadeIn();
        }
        scrollLength=$(this).scrollTop();
    })
    updateQuestion(1);
    getLoginUserInfo();
})
function updateQuestion(page) {
    /*****************questions*****************/
    $.ajax({
        url:"/search/",
        data:{
            "page":page
        },
        type:"get",
        success:function (data) {
            $(".table tbody").empty();
            var s1="<tr><th scope=\"row\">";
            var s2="</th><td><a href=\"/question_detail/";
            var s3="\">";
            var s4="</a></td><td>";
            var s5="</td></tr>";
            for (var i=0;i<data.length;i++){
                var s=s1+data[i]["id"]+s2+data[i]["id"]+s3+data[i]["title"]+s4+data[i]["submit"]+s5;
                $(".table tbody").append(s);
            }
        }
    })
    /*********************page numbers********************/
    $.ajax({
        url:"/getpageamount/",
        type:"get",
        success:function (data) {
            $(".pagination").empty();
            var i=1;
            for (;i<=data/20+(data%20>0);i++){
                var s="<li";
                if (i==page){
                    s+=" class=\"active\"";
                }
                s+="><a onclick=\"updateQuestion("+i+")\" href=\"javascript:void(0)\">";
                s+=i;
                if (i==page){
                    s+=" <span class=\"sr-only\">(current)</span>";
                }
                s+="</a></li>";
                $(".pagination").append(s);
            }
        }
    })
}
var getLoginUserInfo=function () {
    $.ajax({
        url: "/getUserInfo",
        type: "get",
        success:function (data) {
            if (data.length==0){/*do nothing*/
            }else{
                if (data["headImage"]!="default.jpg") {
                    $(".user-head-div").css("background", "url(/resources/img/head/" + data["headImage"] + ")");
                }
                $("#user-information-div-nickname").html(data["nickname"]);
                $("#acAmount").html(data["ac"]);
            }
        }
    })
}