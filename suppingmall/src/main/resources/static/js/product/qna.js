function getCreateQnaFom(productId) {
    window.open("/products/"+productId+"/qna/form","_blank","width=500,height=600,left=500,top=100,menubar=no,toolbar=no,location=no");
    window.opener.location.href= "/products/"+productId+"/#qna";
}


function getReplyList(page) {
    const productId = document.getElementById('productId').value;

    $.ajax({
        url: "/products/"+productId+"/qna/"+page,
        type: 'GET',
        contentType: 'application/json',
        success: (dataList)=> {
            setReplyList(dataList.list);
            setPageList(dataList.qnaPageMaker);
        },
        error: ()=> {

        }
    });

}

function setReplyList(list){
    var str = "";
    let sellerId = document.getElementById('sellerId').value;
    let userId = null;
    if(document.getElementById('userId') !== null){
        userId = document.getElementById('userId').value;
    }

    list.forEach(function (qna) {


        str += '<ul class="tableRow">\n' +
            '<li id="qna-'+qna.qnaId+'" class="bg-light collapse border-top border-bottom p-3">\n' +
            '       <div class="row p-3">\n' +
            '            <div class="cell replyYn text-center">\n' +
            '                 <svg width="3em" height="3em" viewBox="0 0 16 16" class="bi bi-patch-question" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '                      <path d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM8.05 9.6c.336 0 .504-.24.554-.627.04-.534.198-.815.847-1.26.673-.475 1.049-1.09 1.049-1.986 0-1.325-.92-2.227-2.262-2.227-1.02 0-1.792.492-2.1 1.29A1.71 1.71 0 0 0 6 5.48c0 .393.203.64.545.64.272 0 .455-.147.564-.51.158-.592.525-.915 1.074-.915.61 0 1.03.446 1.03 1.084 0 .563-.208.885-.822 1.325-.619.433-.926.914-.926 1.64v.111c0 .428.208.745.585.745z"/>\n' +
            '                      <path fill-rule="evenodd" d="M10.273 2.513l-.921-.944.715-.698.622.637.89-.011a2.89 2.89 0 0 1 2.924 2.924l-.01.89.636.622a2.89 2.89 0 0 1 0 4.134l-.637.622.011.89a2.89 2.89 0 0 1-2.924 2.924l-.89-.01-.622.636a2.89 2.89 0 0 1-4.134 0l-.622-.637-.89.011a2.89 2.89 0 0 1-2.924-2.924l.01-.89-.636-.622a2.89 2.89 0 0 1 0-4.134l.637-.622-.011-.89a2.89 2.89 0 0 1 2.924-2.924l.89.01.622-.636a2.89 2.89 0 0 1 4.134 0l-.715.698a1.89 1.89 0 0 0-2.704 0l-.92.944-1.32-.016a1.89 1.89 0 0 0-1.911 1.912l.016 1.318-.944.921a1.89 1.89 0 0 0 0 2.704l.944.92-.016 1.32a1.89 1.89 0 0 0 1.912 1.911l1.318-.016.921.944a1.89 1.89 0 0 0 2.704 0l.92-.944 1.32.016a1.89 1.89 0 0 0 1.911-1.912l-.016-1.318.944-.921a1.89 1.89 0 0 0 0-2.704l-.944-.92.016-1.32a1.89 1.89 0 0 0-1.912-1.911l-1.318.016z"/>\n' +
            '                 </svg>\n' +
            '             </div>\n' +
            '             <div class="cell qnaTitle">'+qna.title+'</div>\n' +
            '             <div class="cell creator"> </div>\n' +
            '             <div class="cell createdDate"> </div>\n' +
            '        </div>';

        if(qna.reply !== null) {
            str += '        <div class="row border-top p-3">\n' +
                '             <div class="cell replyYn text-center">\n' +
                '                  <svg width="3em" height="3em" viewBox="0 0 16 16" class="bi bi-info-circle" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                '                       <path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>\n' +
                '                       <path d="M8.93 6.588l-2.29.287-.082.38.45.083c.294.07.352.176.288.469l-.738 3.468c-.194.897.105 1.319.808 1.319.545 0 1.178-.252 1.465-.598l.088-.416c-.2.176-.492.246-.686.246-.275 0-.375-.193-.304-.533L8.93 6.588z"/>\n' +
                '                       <circle cx="8" cy="4.5" r="1"/>\n' +
                '                   </svg>\n' +
                '              </div>\n' +
                '              <div class="cell qnaTitle">'+qna.reply.title+'</div>\n' +
                '              <div class="cell creator">판매자</div>\n' +
                '             <div class="cell createdDate">'+qna.reply.createdDate+'</div>\n' +
                '           </div>';
        } else if(userId === sellerId){
            str += '        <div class="row border-top p-3" data-rno="'+qna.qnaId+'">\n' +
                '                <div class="cell replyYn text-center"></div>\n' +
                '                     <div class="cell qnaTitle">\n' +
                '                          <textarea class="form-control replyTitle"></textarea>\n' +
                '                     </div>\n' +
                '                 <div class="cell creator">\n' +
                '                     <button class="btn-primary btn-sm replyButton p-3">작성하기</button>\n' +
                '                  </div>\n' +
                '                  <div class="cell createdDate"></div>'
                '           </div>';
        }

        str +=  '</li></ul>';
    });

    $('.div.tableRow').after(str);
}

function setPageList(pageMaker) {
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

    $('.qnaPagination').html(str);


}

function setReply(title,qnaId) {

    var str = "";

    str +=
        '             <div class="cell replyYn text-center">\n' +
        '                  <svg width="3em" height="3em" viewBox="0 0 16 16" class="bi bi-info-circle" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
        '                       <path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>\n' +
        '                       <path d="M8.93 6.588l-2.29.287-.082.38.45.083c.294.07.352.176.288.469l-.738 3.468c-.194.897.105 1.319.808 1.319.545 0 1.178-.252 1.465-.598l.088-.416c-.2.176-.492.246-.686.246-.275 0-.375-.193-.304-.533L8.93 6.588z"/>\n' +
        '                       <circle cx="8" cy="4.5" r="1"/>\n' +
        '                   </svg>\n' +
        '              </div>\n' +
        '              <div class="cell qnaTitle">'+title+'</div>\n' +
        '              <div class="cell creator">판매자</div>\n' +
        '             <div class="cell createdDate">'+new Date()+'</div>';

    document.getElementById('qna-'+qnaId).children[1].innerHTML = str;

}

function createReply(qnaId,replyTitle){

    let userId = document.getElementById('userId').value;
    let sellerId = document.getElementById('sellerId').value;
    let productId = document.getElementById('productId').value;

    if(userId !== sellerId) {
        alert("작성할 수 없습니다.");
        return ;
    }

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var reply = {
        "userId" : sellerId,
        "title" : replyTitle
    }

    $.ajax({
        url: "/products/"+productId+"/qna/"+qnaId+"/reply",
        type: 'POST',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type","application/json");
            xhr.setRequestHeader(header,token);
        },
        data : JSON.stringify(reply),
        success: ()=> {
            alert("답변 작성완료");
            setReply(replyTitle,qnaId);
        },
        error: ()=> {
            alert("답변 작성실패");
        }
    })

}


$(".qnaPagination").on("click","li a", function (event){
    event.preventDefault();
    var page = event.target.getAttribute('href');
    getReplyList(page);
});


$(document).on("click",'.replyButton',function (event){
    let button = event.target;
    var qnaId = button.parentNode.parentElement.getAttribute('data-rno');
    var replyTitle = button.parentNode.parentElement.getElementsByClassName('replyTitle')[0].value;

    createReply(qnaId,replyTitle);
});
