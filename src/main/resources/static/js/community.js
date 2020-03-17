/**
 *提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val().trim();
    comment2target(questionId,1,content);
    // console.log(questionId);
    // console.log(content);
}

function comment2target(targetId,type,content) {
    if(!content){
        alert("不能回复空内容！！！");
        return;
    }
    $.ajax({

        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
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
}
function comment(e) {
    var commentId=e.getAttribute("data-id");
    var content=$("#input-"+commentId).val().trim();
    comment2target(commentId,2,content);
    console.log(commentId);
    console.log(content);
}

/**
 *展开二级回复
 */

function collapseComments(e) {
    var id=e.getAttribute("data-id");
    var comments=$("#comment-"+id);

    var collapse=e.getAttribute("data-collapse");
    if(collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }else {
            //展开二级评论
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object avatar-2",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
            });
            comments.addClass("in");
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }
    }
}