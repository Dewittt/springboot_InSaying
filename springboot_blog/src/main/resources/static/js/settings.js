$(function() {
    var username = $("#aaa").text();
    if(username==null||username==undefined||username==""){
        var center = document.getElementById("message");
        var setting = document.getElementById("settings");
        var exit = document.getElementById("exit");
        center.style.display='none';
        exit.style.display='none';
        setting.style.display='none';
    }else{
        var register=document.getElementById("register");
        var login=document.getElementById("login");
        register.style.display='none';
        login.style.display='none';
    }

    $('#inputGroupFileAddon02').click(function () {
            var formData = new FormData();
            formData.append("file", $("#inputGroupFile02")[0].files[0]);
            $.ajax({
                url: "/uploadimg",
                type: "post",
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    window.alert("上传成功");
                    window.location.href="/index";
                }
            })
        });
});