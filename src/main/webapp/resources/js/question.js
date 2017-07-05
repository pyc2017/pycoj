var submit=function (id) {
    $.ajax({
        url: "/submit/"+id,
        type: "post",
        data:{
            "code": $("#code").val(),
            "lang": $("#lang").val()
        },
        success: function (data) {
            $(".close").click();
            $("a[href='#result']").click();
            $("#state_refresh_button").click();
        },
        beforeSend: function () {
            waiting();
        }
    })
}
var waiting=function () {
    $(".states").empty();
    $(".states").append("<div class=\"col-md-4\"></div><div class=\"load-3 col-md-4\"></div><div class=\"col-md-4\"></div>");
    for (var i=1;i<=8;i++) {
        $(".load-3").append("<div class=\"k-line2 k-line12-"+i+"\"></div>");
    }
}
var refresh=function (id) {
    $.ajax({
        url: "/submit/ask/"+id,
        type: "get",
        success: function (data) {
            if (data==null||data.length==0){
                waiting();
            }else{
                $(".states").empty();
                for (var i=1;i<=data.length;i++){
                    if (data[i-1]["info"]=="accepted"){
                        $(".states").append("<div class=\"state-success state\"><h4>CASE "+i+":Accepted</h4><p>"+data[i-1]["timeCost"]+"ms&nbsp;"+data[i-1]["memoryCost"]+"kb</p></div>");
                    }else{
                        $(".states").append("<div class=\"state-fail state\"><h4>CASE "+i+"</h4><p>"+data[i-1]["info"]+"</p>");
                    }
                }
            }
        }
    })
}