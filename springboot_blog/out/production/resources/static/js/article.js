$(function(){
    var username = $("#aaa").text();
    if(username==null||username==undefined||username==""){
        var center = document.getElementById("message");
        var setting = document.getElementById("settings");
        var exit = document.getElementById("exit");
        var article = document.getElementById("write");
        center.style.display='none';
        exit.style.display='none';
        setting.style.display='none';
        article.style.display='none';
    }else{
        var register=document.getElementById("register");
        var login=document.getElementById("login");
        register.style.display='none';
        login.style.display='none';
    }
});


