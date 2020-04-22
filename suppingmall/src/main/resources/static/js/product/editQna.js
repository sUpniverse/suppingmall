function updateQna(boardId) {
    window.open("/products/qnas/"+boardId+"/updateForm","_blank","width=500,height=600,left=500,top=100,menubar=no,toolbar=no,location=no");
}

function deleteQna(boardId) {
    if(confirm('삭제하시겠습니까?')) {
        $.ajax({
            url: "/products/qnas/"+boardId,
            type: "delete",
            contentType: "application/json",
            success: () => {
                console.log('삭제완료');
                document.getElementById('qnaDetail'+boardId).remove();
                document.getElementById('qna'+boardId).remove();
            },
            error: () => {

            }
        })
    }
}