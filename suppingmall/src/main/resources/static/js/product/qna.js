function getCreateQnaFom(productId) {
    window.open("/products/"+productId+"/qnas/form","_blank","width=500,height=600,left=500,top=100,menubar=no,toolbar=no,location=no");
}

function getQna(boardId,userId) {

    var detail = document.getElementById('qnaDetail'+boardId);
    if(detail !== null) {
        if(detail.style.display === 'none') {
            detail.style.display = '';
        } else {
            detail.style.display = 'none';
        }
        return;
    }

    var title;
    var comment;

    $.ajax({
        url:"/products/qnas/"+boardId,
        type: 'GET',
        contentType: "application/json",
        success: (data) => {
            title  = data.title;
            comment;
            var isCreator = false;

            if(userId !== undefined || userId !== null) {

                var creatorId = data.creator.userId;
                if(creatorId === userId) {
                    isCreator = true;
                }
            }
            if(data.comments[0] > 0) {
                comment = data.comments[0].title;
            }

            setQnaDetail(title,comment,boardId,isCreator);
        },
        error: () => {

        }
    })
}

function setQnaDetail(title,comment,boardId,isCreator) {
    var answer;

    var edit = '<div class="col-md-6 offset-md-1 text-muted text-sm">' +
                '<a href="javascript:updateQna('+boardId+')">수정</a>' +
                '<span class="mx-3">|</span>' +
                '<a href="javascript:deleteQna('+boardId+')">삭제</a>' +
               '</div>';

    var question = '<div class="question border-bottom">' +
                      '<p class="col-md-6 offset-md-1">'+title+'</p>'

    if(isCreator) {
        question += edit;
    }

    question +='</div>';

    var qna = '<tr class="bg-light" id="qnaDetail'+boardId+'">' +
        '<td colspan="4">' +
        question;

    if(comment === undefined) {
        qna += '</td></tr>'
    } else {

        answer = '<div class="answer">' +
                  '<p class="col-md-6 offset-md-1">'+comment+'</p>' + edit +
                 '</div>';

        qna +=    answer + '</td></tr>';
    }

    $('#qna'+boardId+'').after(qna);
}