$(function () {
    var _pageSize;

    function getBlogsByName(pageIndex,pageSize) {
        $.ajax({
            url:"/u/"+username+"/blogs",
            contentType:'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "catalog":null,
                "keyword":$("#keyword").val()
            },
            success:function (data) {
                $("#mainContainer").html(data);
                if (catalogId){
                    $(".nav-item .nav-link").removeClass("active");
                }
            },
            error:function () {
                toastr.error("个人主页内容访问失败");
            }
        });
    }

    $.tbpage("#mainContainer",function (pageIndex,pageSize) {
        getBlogsByName(pageIndex,pageSize);
        _pageSize = pageSize;
    });

    $("#searchBlogs").click(function () {
        getBlogsByName(0,_pageSize);
    });

    $(".nav-item .nav-link").click(function () {
        var url = $(this).attr("url");
        $(".nav-item .nav-link").removeClass("active");
        $(this).addClass("active");
        $.ajax({
            url:url+'&async=true',
            success:function (data) {
                $("#mainContainer").html(data);
            },
            error:function () {
                toastr.error("请求错误");
            }
        });

        $("#keyword").val('');
    });


});