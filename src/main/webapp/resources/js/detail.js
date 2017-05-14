$(document).ready(function () {
    $("#question-submit-button input[type='image']").click(function () {//开启模态框
        $(".modal").css("visibility","visible");
        $(".modal-data").css("visibility","visible");
    })
    $(".modal-close button").click(function () {
        $(".modal").css("visibility","hidden");
        $(".modal-data").css("visibility","hidden");
    })
})