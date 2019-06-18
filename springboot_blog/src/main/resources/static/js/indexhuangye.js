$(function(){
    var url = window.location.href;
    var index =url.lastIndexOf("/")+1;
    var page = url.substring(index,url.length);

    if (page=="0"||page=="0#"){
        $("#previous").addClass("disabled");
    }



    $("#previous").click(function () {
        if (page!="0"&&page!="0#") {
            window.location.href = url.substring(0, index) + (parseInt(page) - 1).toString();
        }
    });

    $("#next").click(function () {
        if (page!="0"&&page!="0#") {
            window.location.href = url.substring(0, index) + (parseInt(page) + 1).toString();
        }
    });
});


