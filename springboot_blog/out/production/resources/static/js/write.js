$(function(){
	$("#submit").click(function (){
		var editor = window.editor;
		var title = $("#title1").val();
		var shortcut = $('#shortcut1').val();
		var htmlContent = editor.getData();
		var data = {"title":title,"shortcut":shortcut,"htmlContent":htmlContent};
		if (htmlContent!=null&&title!=null){
			$.ajax({
				url:"/article/",
				type:"POST",
				async:false,
				data:JSON.stringify(data),
				contentType:"application/json;charset=utf-8",
				success:function (data) {
					window.location.href='/index';
				}
			})
		}
	});

});

