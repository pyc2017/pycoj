<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<% String path=request.getContextPath();%>
<html>
<head>
    <script src="<%=path%>/resources/js/jquery-2.0.0.min.js"></script>
    <link href="<%=path%>/resources/css/loading.css" rel="stylesheet" type="text/css">
    <jsp:include page="../boostrap.jsp"></jsp:include>
    <link type="text/css" rel="stylesheet" href="/resources/css/fileinput.min.css">
    <link href="<%=path%>/resources/css/index.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/resources/css/detailcomment.css" rel="stylesheet" type="text/css">
    <script src="<%=path%>/resources/js/marked.min.js"></script>
    <script src="<%=path%>/resources/js/detail.js"></script>
    <script src="<%=path%>/resources/js/index.js"></script>
    <script src="/resources/js/fileinput.min.js"></script>
    <script src="/resources/js/fileinput_locale_zh.js"></script>
    <title>${question.title}</title>
</head>
<body>
    <jsp:include page="../nav.jsp"></jsp:include>
    <div class="row myRow">
        <div class="col-md-2"></div>
        <div class="container col-md-6 shadow">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#question" aria-controls="question" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span>&nbsp;Description</a></li>
                <li role="presentation"><a href="#result" aria-controls="result" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;Result</a></li>
                <li role="presentation"><a href="#comments" aria-controls="comments" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;Comments</a></li>
            </ul>
            <div role="tabpanel" class="tab-pane active" id="question">
                <h2>${question.title}</h2>
                <h3>Description</h3>
                <pre>${question.description}</pre>
                <h3>Input</h3>
                <pre>${question.input}</pre>
                <h3>Output</h3>
                <pre>${question.output}</pre>
                <h3>Hint</h3>
                <pre>${question.hint}</pre>
                <div style="margin-top: 30px;">
                    <button type="button" class="btn btn-lg" data-toggle="modal" data-target="#myModal">
                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                    </button>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade wrapp" id="result">
                <div class="states row"></div>
                <div id="state_refresh_button_block">
                    <a id="state_refresh_button" href="javascript:void(0)"><span class="glyphicon glyphicon-refresh" aria-hidden="true" onclick="refresh(${question.id})"></span></a>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade" id="comments">
                <div id="comment-box" class="comment">
                    <div class="comment-left"><img src="/resources/img/head/0.jpg"></div>
                    <div class="comment-right">
                        <div id="comment-box-header">
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#comment-textarea" aria-controls="comment-textarea" role="tab" data-toggle="tab">write</a></li>
                                <li role="presentation"><a href="#comment-show" aria-controls="comment-show" role="tab" data-toggle="tab" id="show">show comment</a></li>
                                <li><a href="javascript:void(0)" title="Add Header" class="js-decoreate" id="AddHeader">H</a></li>
                                <li><a href="javascript:void(0)" title="Add Italic text" class="js-decoreate" id="AddIta"><i>i</i></a></li>
                                <li><a href="javascript:void(0)" title="Add bold text" class="js-decoreate" style="padding-right:15px;" id="AddBold"><strong>B</strong></a></li>
                                <li><a href="javascript:void(0)" title="Insert a quote" class="js-decoreate" id="InsertQ"><strong>“</strong></a></li>
                                <li><a href="javascript:void(0)" title="Insert code" class="js-decoreate" style="padding-right:15px;" id="InsertC"><strong>&lt;&gt;</strong></a></li>
                                <li><a href="javascript:void(0)" title="Add a bulleted list" class="js-decoreate" id="AddBull">·-</a></li>
                                <li><a href="javascript:void(0)" title="Add a numbered list" class="js-decoreate" id="AddNum">1.</a></li>
                                <li><a href="javascript:void(0)" title="Add a link" class="js-decoreate" id="AddURL">URL</a></li>
                                <li><a href="javascript:void(0)" title="Add a tast list" class="js-decoreate" id="AddTa"><span class="glyphicon glyphicon-ok"></span></a></li>
                                <li><a data-target="#uploadModal" data-toggle="modal"><span class="glyphicon glyphicon-picture"></span></a></li>
                            </ul>
                        </div>
                        <div id="comment-box-content">
                            <div role="tabpanel" class="tab-pane active" id="comment-textarea">
                                <textarea name="content" id="oj_content"></textarea>
                                <button class="btn btn-success" type="button" onclick="comment(${question.id})">Comment</button>
                            </div>
                            <div role="tabpanel" class="tab-pane fade comment-content" id="comment-show"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4"></div>
    </div>
    <jsp:include page="../foot.jsp"></jsp:include>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">${question.title}</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="code">Code</label>
                        <textarea type="email" class="form-control" id="code" rows="8"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="lang">编译器</label>
                        <select class="form-control" id="lang">
                            <option value="0">gcc</option>
                            <option value="1">JDK1.8</option>
                            <option value="2">g++</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn submit" id="submit" onclick="submit(${question.id})">Submit</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="uploadModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="uploadModalLabel">上传图片</h4>
                </div>
                <div class="modal-body">
                    <label for="pic">Picture</label>
                    <input type="file" id="pic" name="pic" class="projectfile">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn submit" id="upload" onclick="upload()">Upload</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="uploadWaiting" tabindex="-1" role="dialog" aria-labelledby="uploadWaitingLabel">
        <div class="load-3" style="margin-top:25%;margin-left: 42%;">
                <div class="k-line2 k-line12-1"></div>
                <div class="k-line2 k-line12-2"></div>
                <div class="k-line2 k-line12-3"></div>
                <div class="k-line2 k-line12-4"></div>
                <div class="k-line2 k-line12-5"></div>
                <div class="k-line2 k-line12-6"></div>
                <div class="k-line2 k-line12-7"></div>
                <div class="k-line2 k-line12-8"></div>
        </div>
    </div>
</body>
</html>