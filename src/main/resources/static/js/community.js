function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val().trim();
    if(!content){
        alert("不能回复空内容！！！");
        return;
    }
    $.ajax({

        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                // $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=4300cf1bae128fc6eb78&redirect_uri=http://localhost:8888/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true"); //登录后的新页面根据传参判断是否自动关闭新页面
                    }
                }else {
                    alert(response.message);
                }
            }
        },
        dataType: "json",
        contentType: "application/json"
    });
    console.log(questionId);
    console.log(content);
}