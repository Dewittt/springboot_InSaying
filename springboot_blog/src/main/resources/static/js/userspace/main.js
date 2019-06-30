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

    function convertBase64UrlToBlob(urlData){
        var bytes = window.atob(urlData.split(",")[1]);
        var a = new ArrayBuffer(bytes.length);
        var b = new Uint8Array(a);
        for (var i=0;i<bytes.length;i++){
            b[i] = bytes.charCodeAt(i);
        }
        return new Blob([a],{type:'image/png'});
    }

    $("#submitEditAvatar").on("click",function () {
       var form = $('#avatarformid')[0];
       var formData = new FormData(form);
       var base64Codes = $(".cropImg>img").attr("src");
       formData.append("file",convertBase64UrlToBlob(base64Codes));

       $.ajax({
           url:fileServerUrl,
           type:'post',
           cache:false,
           processData:false,
           contentType:false,
           success:function (data) {
               var avatarUrl = data;
               var csrfToken = $("meta[name='_csrf']").attr("content");
               var csrfHeader = $("meta[name='_csrf_header']").attr("content");
               $.ajax({
                   url:avatarApi,
                   type:'post',
                   contentType:"application/json;charset=utf-8",
                   data:JSON.stringify({"id":Number($("#userId").val()),"avatar":avatarUrl}),
                   beforeSend:function (request) {
                       request.setRequestHeader(csrfHeader,csrfToken);
                   },
                   success:function (data) {
                       if (data.success){
                           $(".blog-avatar").attr("src",data.avatarUrl);
                           toastr.success("保存成功！");
                       } else{
                           toastr.error("获取data.avatarUrl错误!");
                       }
                   },
                   error:function () {
                       toastr.error("访问/u/username/avatar失败!");
                   }
               });
           },
           error:function () {
               toastr.error("post访问fileServerUrl失败!");
           }
       });
    });

});