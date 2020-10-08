function getCommentList(page) {
    const boardId = document.getElementById('boardId').value;

    $.ajax({
        url: "/comments?boardId="+boardId+"&&page="+page,
        type: 'GET',
        contentType: 'application/json',
        success: (dataList) => {
            setCommentList(dataList.list);
            setCommentPageList(dataList.pageMaker);
        },
        error: () => {

        }
    });

}

function setCommentList(list){
    var str = "";
    var user = document.getElementById('userId');
    list.forEach(function (comment) {
        str += '<div class="media pb-3" data-no="'+comment.commentId+'">\n' +
            '        <div class="media-body">\n' +
            '            <div class="bg-light mt-0 d-flex justify-content-between align-items-center w-100">\n' +
            '                <strong class="ml-2" value="comment.creator.userId">'+comment.creator.nickName+'</strong>\n' +
            '                <small>'+convertLocalDateTime(comment.createdDate)+'</small>\n' +
            '             </div>\n';
        if(user !== null && comment.creator.userId == user.value){
            str+= '             <div class="btn-group float-right">\n' +
                '                  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle"data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                '                    수정 / 삭제 / 신고\n' +
                '                  </button>\n' +
                '                  <div class="dropdown-menu">\n' +
                '                       <button type="button" class="dropdown-item" id="comment-update-button">수정</button>\n' +
                '                       <button type="button" class="dropdown-item" id="comment-delete-button">삭제</button>\n' +
                '                  </div>\n' +
                '              </div>\n'
        }
        str +='              <p class="ml-2">'+comment.contents+'</p>\n' +
            '          </div>\n' +
            '    </div>';
    });

    $('#comments').empty();
    $('#comments').append(str);

}


function setCommentPageList(pageMaker) {
    var str = "";

    if(pageMaker.prev){
        str += '<li class="page-item"><a class="page-link"  href="'+(pageMaker.startPage - 1)+'" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>'
    }

    for(var i = pageMaker.startPage; i <= pageMaker.endPage; i++) {
        var strClass = pageMaker.criteria.page === i? 'class=active' : '';
        str += '<li class="page-item '+strClass+'"><a class="page-link" href="'+i+'">'+i+'</a></li>'
    }


    if(pageMaker.next){
        str += '<li class="page-item"><a class="page-link"  href="'+(pageMaker.endPage - 1)+'" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>'
    }

    $('.commentPagination').html(str);
}


$(".commentPagination").on("click","li a", function (event){
    event.preventDefault();
    var page = event.target.getAttribute('href');
    getCommentList(page);
});



$(document).ready(function (){

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    $("#comment-button").on('click',(e,xhr,options) => {
        var boardId = document.getElementById('boardId').value;
        var contents = $('#comment-text').val();
        var comment = {
            "board" : {
                "boardId": boardId
            },
            "contents": contents
        };
        $.ajax({
            type:"POST",
            url:"/comments",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader(header,token);
            },
            dataType: 'text',
            data: JSON.stringify(comment),
            contentType: 'application/json',
            success: (data) => {
                var response = JSON.parse(data);
                var comment_div = '<div id="comments"> </div>';
                var comment_contents = response.contents;
                var comment_user_name = response.creator.nickName;
                var comment =
                    '<div class="media pb-3" data-no="'+response.commentId+'">\n' +
                    '        <div class="media-body">\n' +
                    '            <div class="bg-light mt-0 d-flex justify-content-between align-items-center w-100">\n' +
                    '                <strong class="ml-2">'+response.creator.nickName+'</strong>\n' +
                    '                <small>'+convertLocalDateTime(response.createdDate)+'</small>\n' +
                    '             </div>\n' +
                    '             <div class="btn-group float-right">\n' +
                    '                  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle"data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                    '                    수정 / 삭제 / 신고\n' +
                    '                  </button>\n' +
                    '                  <div class="dropdown-menu">\n' +
                    '                       <button type="button" class="dropdown-item" id="comment-update-button">수정</button>\n' +
                    '                       <button type="button" class="dropdown-item" id="comment-delete-button">삭제</button>\n' +
                    '                  </div>\n' +
                    '              </div>\n' +
                    '              <p class="ml-2">'+response.contents+'</p>\n' +
                    '          </div>\n' +
                    '    </div>';

                $("#comments").append(comment);
            },
            error: () => {
                alert('댓글 생성오류');
            }

        });
    });


    $(document).on('click',"#comment-text",() => {
        if(document.getElementById('userId') === null) {
            var check = confirm("로그인 하시겠습니까?");
            if(check == true) {
                window.location.href='/users/loginform';
            }
        }
    });


    var text = 0;

    $(document).on('click',"#comment-update-button",(e)=> {
        var comment = e.target.parentElement.parentElement.parentElement.parentElement;
        var user_id = e.target.parentElement.parentElement.previousElementSibling.getAttribute('value');
        var comment_text = comment.getElementsByTagName("p").item(0);
        text = comment_text.innerText;
        comment_text.innerHTML =
            '<div class="form-group">' +
            '<textarea class="form-control" id="update-comment-text" rows="3" >'+text+'</textarea>' +
            '<button type="button" class="btn btn-success btn-sm mr-1" id="update-comment-button">수정하기</button>' +
            '<button type="button" class="btn btn-light btn-sm" id="cancel-comment-button">취소하기</button>'
        '</div>';
    });


    $(document).on('click',"#comment-delete-button",(e,xhr,options)=> {
        var comment = e.target.parentElement.parentElement.parentElement.parentElement;
        var comment_id = comment.getAttribute('data-no');
        $.ajax({
            type:"DELETE",
            url:"/comments/"+comment_id,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader(header,token);
            },
            success: () => {
                comment.remove();
            }
        });
    });

    $(document).on('click',"#update-comment-button",(e,xhr,options)=> {
        var comment = e.target.parentElement.parentElement.parentElement.parentElement;
        var comment_id = comment.getAttribute('data-no');
        var comment_text = comment.getElementsByTagName("p").item(0);
        var message = e.target.parentElement.getElementsByTagName("textarea").item(0).value;
        $.ajax({
            type:"PUT",
            url:"/comments/"+comment_id,
            dataType: 'text',
            data: JSON.stringify({
                "contents": message
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader(header,token);
            },
            success: () => {
                comment_text.innerText = message;
            }
        });

    });

    $(document).on('click',"#cancel-comment-button",(e)=> {
        var comment = e.target.parentElement.parentElement.parentElement.parentElement;
        var comment_text = comment.getElementsByTagName("p").item(0);
        comment_text.innerText = text;
        text = "";
    });
});