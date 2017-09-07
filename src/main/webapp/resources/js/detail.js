$(document).ready(function () {
    textarea=$('textarea')[0];
    var start=0,end=0;
    getComment(window.location.pathname.substring(17));
    $("#question-submit-button input[type='image']").click(function () {/*开启模态框*/$('.modal').css('visibility','visible');$(".modal-data").css("visibility","visible");});
    $(".modal-close button").click(function(){$(".modal").css("visibility","hidden");$(".modal-data").css("visibility","hidden");});
    $(textarea).blur(function(){start=textarea.selectionStart;end=textarea.selectionEnd;});
    $("#AddHeader").click(function(){renderTheTextArea(textarea,start,end,"# ","");});
    $("#AddIta").click(function(){renderTheTextArea(textarea,start,end,'_','_');});
    $("#AddBold").click(function(){renderTheTextArea(textarea,start,end,'**','**');});
    $("#InsertQ").click(function(){renderTheTextArea(textarea,start,end,'\r\n> ','');});
    $("#InsertC").click(function(){renderTheTextArea(textarea,start,end,'`','`');});
    $("#AddBull").click(function(){renderTheTextArea(textarea,start,end,'\r\n- ','');});
    $("#AddNum").click(function(){renderTheTextArea(textarea,start,end,'\r\n1. ','');});
    $("#AddTa").click(function(){renderTheTextArea(textarea,start,end,'\r\n- [] ','');});
    $("#AddURL").click(function(){renderTheTextArea(textarea,start,end,'[](url)','');});
    $('#show').click(function(){$('#comment-show').empty();$('#comment-show').html(marked(textarea.value));});
})
var textarea;
var renderTheTextArea=function(t,s,e,p,suffix){var oldString=t.value;var selectedText=oldString.substring(s,e);selectedText=p+selectedText+suffix;$(t).val(oldString.substring(0,s)+selectedText+oldString.substring(e,oldString.length));t.focus();t.setSelectionRange(s+p.length,e+p.length);}
var comment=function(id){
    $.ajax({
        url:'/comment/'+id,
        type:'post',
        data:{
            'content':marked(textarea.value)
        },
        success:function(data){
            if(data.success===false&&data.info==='access denied'){
                window.location='/login/?original=/question_detail/'+id;
            }else if(data.success==true){
                console.log('success');
            }
        }
    })
};
var getComment=function(id){
    $.ajax({
        url:'/comment/get/'+id,
        type:'get',
        success:function (data) {
            var a=data.data;var s;
            for (var i=0;i<a.length;i++){
                s='<div class="comment"><div class="comment-left"><img src="/resources/img/head/'+
                    a[i].coder.headImage+
                    '"></div><div class="comment-right"><div class="comment-info"><span class="comment-info-nickname">'+
                    a[i].coder.nickname+
                    '</span><span> comment on '+new Date(a[i].createTime).toGMTString()+
                    '</span></div><div class="comment-content">'+
                    a[i]["content"]+'</div></div></div>';
                $('#comment-box').before(s);
            }
        }
    })
};
var submit=function(id){
    /*提交前先建立websocket连接，方便之后对解题信息的传输，避免轮询*/
    ws=new NormalSolutionWS(id);
};
var waiting=function(){
    $('.states').empty();
    $('.states').append('<div class="col-md-4"></div><div class="load-3 col-md-4"></div><div class="col-md-4"></div>');
    for (var i=1;i<=8;i++){
        $('.load-3').append('<div class="k-line2 k-line12-'+i+'"></div>');
    }
};
var refresh=function(id){
    $.ajax({
        url:"/submit/ask/"+id,
        type:"get",
        success:function(data){
            if(data==null||data.length==0){
                waiting();
            }else{
                $(".states").empty();
                for (var i=1;i<=data.length;i++){
                    if(data[i-1].state==0){
                        $('.states').append(
                            '<div class="state-success state well"><h4>CASE '+
                            i+
                            ':Accepted</h4><p>'+
                            data[i-1].timeCost+
                            'ms&nbsp;'+
                            data[i-1].memoryCost+
                            'kb</p></div>');
                    }else{
                        $('.states').append(
                            '<div class="state-fail state well"><h4>CASE '+
                            i+
                            '</h4><pre>'+
                            data[i-1].info+'</pre>');
                    }
                }
            }
        }
    })
};
var NormalSolutionWS=function (id) {
    var count=1;
    this._ws=new WebSocket('ws://'+window.location.host+'/normalSolution');
    this._ws.onopen=function(e){
        $.ajax({
            url:'/submit/'+id,
            type:'post',
            data:{
                'code':$('#code').val(),
                'lang':$('#lang').val()
            },
            success: function (d){
                $('.close').click();
                $("a[href='#result']").click();
                $('#state_refresh_button').click();
            },
            beforeSend: function () {
                waiting();
            }
        });
    };
    this._ws.onmessage=function (d) {
        if (count==1) $('.states').empty();
        var obj=JSON.parse(d.data);/*反序列化*/
        if(obj.state==0){
            $('.states').append(
                '<div class="state-success state well"><h4>CASE '+
                count+
                ':Accepted</h4><p>'+
                obj.t+
                'ms&nbsp;'+
                obj.m+
                'kb</p></div>');
        }else{
            $('.states').append('<div class="state-fail state well"><h4>CASE '+
                count+
                '</h4><pre>'+
                obj.info+'</pre>');
        }
        count++;
    };
    this._ws.onerror=function (e) {
        console.log('error');
        window.location='/login/?original=/question_detail/'+id;
    }
    this._ws.onclose=function () {
        console.log('close');
    }
};