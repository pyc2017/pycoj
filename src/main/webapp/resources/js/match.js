var questionIds=new Array();
var getIn=function () {/*get in the match with id and password*/$.ajax({url:"/match",type:"post",data:{"matchId":$("#matchId").val(),"matchPassword":$("#matchPassword").val()},success:function(d){if(d["success"]==false&&d["info"]==="access denied"){window.location="/login/?original=/match";}else if(d["success"]==false&&d["info"]==="denied"){alert("The password is not right!");}else{window.location="/match/index?match="+d["data"]["id"];}}})}
var newMatch=function () {
    var startTimeStamp=Date.parse(new Date($("#startDate").val()+" "+$("#startTime").val()));
    var endTimeStamp=Date.parse(new Date($("#endDate").val()+" "+$("#endTime").val()));
    if (endTimeStamp-startTimeStamp<0){
        alert("Invalid!Please check the date and time.");
        return false;
    }
    $.ajax({
        url:"/match/new",type:"post",data:{"matchName":$("#matchName").val(),"matchPassword":$("#matchPassword2").val(),"startTime":startTimeStamp,"endTime":endTimeStamp},success:function (data) {
            if(data["success"]==false&&data["info"]==="access denied"){window.location="/login/?original=/match";}else if (data["success"]==true){
                alert("Congratulation!\nYou have successfully create a match.\nNow you can add questions for the match.\nThe Match ID:"+data["data"]["id"]+",The password:"+data["data"]["password"]);
            }
        }
    })
};
var getQuestion=function(){
    $.ajax({
        url:'/match/question?m='+getMatchId(),
        type:'get',
        success:function (d) {
            if (d.success==true){
                $("#rankTable thead tr").append('<th>#</th>')
                $('#questionslist').append('<caption><a href="#" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span>Upload My Solution</span></a></caption>');
             //    $("#accordion").empty();
                var s1='<div class="panel panel-default"><div class="panel-heading" role="tab" id="heading',s2='"><h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse',s3='" aria-expanded="false" aria-controls="collapse',s4='">',s5='</a></h4></div><div id="collapse',s6='" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading',s7 = '"><div class="panel-body">', s8 = '</div></div>';
                var body1='<h3>Description</h3><pre>',body2='</pre><h3>Input</h3><pre>',body3='</pre><h3>Output</h3><pre>', body4 = '</pre><h3>Hint</h3><pre>', body5 = '</pre>';
                for (var i = 1; i <= d.data.length; i++) {
                    $('#accordion').append(s1+d.data[i-1].id+s2+i+s3+i+s4+'#'+i+' '+d.data[i-1].title+s5+i+s6+i+s7+body1+d.data[i-1].description+body2+d.data[i-1].input + body3 + d.data[i-1].output+body4+d.data[i-1].hint+body5+s8);
                    $('#questionId').append('<option id="question'+d.data[i-1].id+'" value="'+d.data[i-1].id+'">'+d.data[i - 1].title+'</option>');
                    $('#rankTable thead tr').append('<th>'+d.data[i-1].title+'</th>')
                    questionIds[i-1]=d.data[i-1].id;
                }
                $.ajax({/*获取题目的完成情况*/
                    url:'/match/solved',
                    type:'get',
                    data:{'mid':getMatchId()},
                    success:function(_d){
                        if (_d.success==false&&_d.info==='access denied'){
                            window.location='/login/';
                        }else if (_d.success==true){
                            for (var j=0;j<_d.data.length;j++){
                                $('#heading'+_d.data[j]).addClass('success');
                                var nodea=$('#heading'+_d.data[j]+' a')[0];
                                $(nodea).html(nodea.innerHTML+'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>');
                            }
                        }
                    }
                });
            }else if(d.info==='access denied'){
                window.location='/match';
            }else{}
        }
    });
};
var checkCreator=function(){$.ajax({url:"/match/checkCreator",type:"get",data:{"id":getMatchId()},success:function(d){if(d==true){$("table").prepend('<caption><a href="/new/?m='+getMatchId()+'"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span>New A Question</span></a></caption>');}}})};
var getMatchId=function(){var reg=new RegExp("(^|&)match=([^&]*)(?=(&|$))");var id=window.location.search.substring(1).match(reg)[0].substring(6);return id;};
var submit=function () {
    $.ajax({
        url:'/match/submit/'+getMatchId()+'/'+$("#questionId").val(),
        type:'post',
        data:{
            'code':$('#code').val(),
            'lang':$('#lang').val()
        },
        success:function(d){
            if (d.success==false){
                $('#matchResult').empty();
                $('#matchResult').append(
                    '<div id="finalResult" class="alert alert-danger alert-dismissable" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                    '<strong>Oops!</strong>Your request is illegal or you have solve this problem.The server will not accept any solution after you have been accepted.</div>');
            }
        },
        beforeSend: function () {
            $('.close').click();$('#code').val('');waiting();
            $('#submit').attr('disabled','disabled');
            loop(20);
        }
    })
};
var rank=function (ea,refresh) {
    if (refresh==false) return ;
    $(ea).attr('onclick','rank(this,false)');
    $('#rankTable tbody').empty();
    $.ajax({
        url:'/match/rank',type:'get',data:{'mid':getMatchId()},
        success:function(d){
            /*init*/
            var length=d["data"].length;var start=Math.floor(d["data"][length-1]["submitTime"]);length--;
            var QuestionInfo=function (times,score) {
                this.times=times;
                this.score=score;
                this.hasAC=false;
            }
            var CoderInfo=function (name,score,ac,questionInfos) {
                this.name=name;
                this.score=score;
                this.ac=ac;
                this.questionInfos=questionInfos;
            }
            var coderMap={};//username ---> index
            var coderInfos=new Array(),len=0;
            for (var i=length-1;i>=0;i--){
                var node=d.data[i],value,index;
                if ((index=coderMap[node.username])==null){
                    index=coderMap[node.username]=len;
                    len++;
                    coderInfos[index]=new CoderInfo(node.username,0,0,new Array());
                }
                value=coderInfos[index];
                if (value.questionInfos[node.questionId]==null)
                    value.questionInfos[node.questionId]=new QuestionInfo(0,0);
                if (value.questionInfos[node.questionId].hasAC==true) continue;/*已经ac过了，以后的提交都不算入成绩*/
                else{
                    value.questionInfos[node.questionId].times++;//添加该用户在问题上的提交次数
                    if (node.ac==0){
                        value.questionInfos[node.questionId].hasAC=true;
                        value.questionInfos[node.questionId].score=node.submitTime+1200000*(value.questionInfos[node.questionId].times-1)-start;
                        value.score+=value.questionInfos[node.questionId].score;
                        value.ac++;
                    }
                }
            }
            /*sort*/
            for (var i=coderInfos.length;i>=0;i--){
                for (var j=0;j<i-1;j++){
                    if (coderInfos[j].ac<coderInfos[j+1].ac){
                        var temp=coderInfos[j];
                        coderInfos[j]=coderInfos[j+1];
                        coderInfos[j+1]=temp;
                    }else if (coderInfos[j].ac==coderInfos[j+1].ac&&coderInfos[j].score>coderInfos[j+1].score){
                        var temp=coderInfos[j];
                        coderInfos[j]=coderInfos[j+1];
                        coderInfos[j+1]=temp;
                    }
                }
            }
            /*init html*/
            for (var i=0;i<coderInfos.length;i++){
                var row='<tr><td>'+coderInfos[i].name+'</td>';
                for (var j=0;j<questionIds.length;j++){
                    var temp=coderInfos[i].questionInfos[questionIds[j]];
                    if (temp==null){/*没提交过*/
                       row+='<td>-</td>';
                    }else if (temp.hasAC==false){/*未ac*/
                        if (temp.times>0){
                            row+='<td class="danger">-'+temp.times+'</td>';
                        }else {
                            row += '<td>-</td>';
                        }
                    }else{/*ac*/
                        var s,h,m;
                        s=temp.score/1000;
                        h=parseInt(s/3600);
                        s=s%3600;
                        m=parseInt(s/60);
                        s=Math.floor(s%60);
                        row+='<td class="success">'+h+':'+m+':'+s+'</br>(-'+(temp.times-1)+')</td>'
                    }
                }
                row+='</tr>';
                $("#rankTable tbody").append(row);
            }
            $(ea).attr('onclick','rank(this,true)');
        }
    })
}
var websocket=function () {
    var socket=new WebSocket('ws://'+window.location.host+'/match/c?m='+getMatchId());
    socket.onopen=function(event){
        $('#submit').removeAttr('disabled');
    };
    socket.onmessage=function (d) {
        var acMessage=JSON.parse(d.data);/*反序列化*/
        $('#matchResult').empty();
        switch (acMessage.ac){
            case 0:/*ac*/
                $('#matchResult').append(
                    '<div id="finalResult" class="alert alert-success alert-dismissable" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                    '<strong>Congratulations!</strong> '+$('#question'+acMessage.q).text()+' accepted.</div>');
                $('#heading'+acMessage.q).addClass('success');
                var nodea=$('#heading'+acMessage.q+' a')[0];
                $(nodea).html(nodea.innerHTML+'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>');
                break;
            case 1:
                $('#matchResult').append(
                    '<div id="finalResult" class="alert alert-danger alert-dismissable" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                    '<strong>Oops!</strong> '+$('#question'+acMessage.q).text()+' compilation error.</div>');
                break;
            case 2:
                $('#matchResult').append(
                    '<div id="finalResult" class="alert alert-danger alert-dismissable" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                    '<strong>Oops!</strong> '+$('#question'+acMessage.q).text()+' time limited error.</div>');
                break;
            case 3:
                $('#matchResult').append(
                    '<div id="finalResult" class="alert alert-danger alert-dismissable" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                    '<strong>Oops!</strong> '+$('#question'+acMessage.q).text()+' memory limited eroor.</div>');
                break;
            case 4:
                $('#matchResult').append(
                    '<div id="finalResult" class="alert alert-danger alert-dismissable" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                    '<strong>Oops!</strong> '+$('#question'+acMessage.q).text()+' wrong answer.</div>');
                break;
        }
    };
}
var waiting=function(){
    $('#matchResult').empty();
    $('#matchResult').append('<div class="load-3"></div>');
    for (var i=1;i<=8;i++){
        $('.load-3').append('<div class="k-line2 k-line12-'+i+'"></div>');
    }
};
var loop=function (i) {
    var n=$('#submit');
    if (i<=0){
        n.removeAttr('disabled');
        n.html('Submit');
    }else{
        n.html('Submit <span class="badge">'+i+'</span>');
        setTimeout('loop('+(i-1)+')',1000);
    }
};