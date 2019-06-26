$(function () {
    var avatarApi;

    $(".blog-content-container").on("click",".blog-edit-avatar",function () {
        avatarApi = "/u/"+$(this).attr("userName")+"/avatar";
        $.ajax({
            url:avatarApi,
            success:function (data) {
                $("#avatarFormContainer").html(data);
            },
            error:function () {
                toastr.error("error!");
            }
        });
    });
    

});