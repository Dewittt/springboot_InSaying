$(function () {
    var _pageSize;
    function getUsersByName(pageIndex,pageSize) {
        $.ajax({
            url:"/users",
            contentType:'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "name":$("#searchName").val()
            },
            success:function (data) {
                $("#mainContainer").html(data);
            },
            error:function () {
                toastr.error("error");
            }
        })
    };

    $.tbpage("#mainContainer",function (pageIndex,pageSize) {
        getUsersByName(pageIndex,pageSize);
        _pageSize=pageSize;
    });

    $("#searchNameBtn").click(function () {
        getUsersByName(0,_pageSize);
    });

    $("#addUser").click(function () {
        $.ajax({
           url:"/users/add",
           success:function (data) {
               $("#userFormContainer").html(data);
           }
        });
    });

    $("#rightContainer").on("click",".blog-edit-user", function () {
        $.ajax({
            url:"/users/edit/"+$(this).attr("userId"),
            success:function (data) {
                $("#userFormContainer").html(data);
            },
            error:function () {
                toastr.error("error!");
            }
        })
    });

    $("#submitEdit").click(function () {
       $.ajax({
           url:"/users",
           type:'post',
           data:$('#userForm').serialize(),
           success:function (data) {
               $('#userForm')[0].reset();
               if(data.success){
                   getUsersByName(0,_pageSize);
               }else{
                   toastr.error(data.message);
               }
           }
       })
    });

    $("#rightContainer").on("click",".blog-delete-user",function () {

        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
           url:"/users/"+$(this).attr("userId"),
           type:'DELETE',
            beforeSend: function(request){
               request.setRequestHeader(csrfHeader,csrfToken);
            },
           success:function (data) {
               if (data.success){
                   getUsersByName(0,_pageSize);
               } else{
                   toastr.error(data.message);
               }
           },
           error:function () {
               toastr.error("error!");
           }
       }) ;
    });
});