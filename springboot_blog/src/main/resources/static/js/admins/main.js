$(function () {
    $(".blog-menu .list-group-item").click(function () {
        var url = $(this).attr("url");

        $(".blog-menu .list-group-item").removeClass("active");
        $(this).addClass("active");

        $.ajax({
            url:url,
            success:function (data) {
                $("#rightContainer").html(data);
            },
            error:function () {
                alert("error");
            }
        });
    });


    $(".blog-menu .list-group-item:first").trigger("click");

});