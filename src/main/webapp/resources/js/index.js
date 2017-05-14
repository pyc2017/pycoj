$(document).ready(function () {
    updateQuestion(1);
    $("#pagenumbers div a").click(function () {
        alert($(this).val());
    })
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
            $("#questions ul").empty();
            var s1="<li>"+
                "<div class=\"question\">"+
                "<div class=\"question-a\">"+
                "<a href=\"";
            var s2="\">";
            var s3="</a>"+
                "</div>"+
                "<div class=\"question-submit\">";
            var s4="</div>"+
                "</div>"+
                "</li>";
            for (var i=0;i<data.length;i++){
                var s=s1+"/question_detail/"+data[i]["id"]+s2+data[i]["title"]+s3+data[i]["submit"]+s4;
                $("#questions ul").append(s);
            }
        }
    })
    /*********************page numbers********************/
    $.ajax({
        url:"/getpageamount/",
        type:"get",
        success:function (data) {
            $("#pagenumbers div").empty();
            if (page>1) {
                $("#pagenumbers div").append("<a onclick=\"updateQuestion(" + (page - 1) + ")\" href=\"javascript:void(0)\" class=\"pagenumbers_left\">&lt;&lt;前一页</a>");
            }
            var i=1;
            for (;i<=data/20+(data%20>0);i++){
                var s="<a onclick=\"updateQuestion("+i+")\" href=\"javascript:void(0)\"";
                if (i==page){
                    s+=" class=\"pagenumbers_now\"";
                }
                s+=">";
                s+=i;
                s+="</a>";
                $("#pagenumbers div").append(s);
            }
            $("#pagenumbers div").append("<a onclick=\"updateQuestion("+(page+1)+")\" href=\"javascript:void(0)\" class=\"pagenumbers_right\">后一页&gt;&gt;</a>");
        }
    })
}