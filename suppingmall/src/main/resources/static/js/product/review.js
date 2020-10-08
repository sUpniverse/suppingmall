function getReviewList(page) {
    const productId = document.getElementById('productId').value;

    $.ajax({
        url: "/reviews?productId="+productId+"&&page="+page,
        type: 'GET',
        contentType: 'application/json',
        success: (dataList) => {
            setReviewList(dataList.list);
            setReviewPageList(dataList.reviewPageMaker);
        },
        error: () => {

        }
    });

}

function setReviewList(list){
    var str = "";

    list.forEach(function (review) {
        str += '<div class="media text-muted pt-1 border-bottom  border-gray">\n' +
            '        <svg class="bd-placeholder-img mr-2 bi bi-person-circle py-2" width="5em" height="5em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '             <path d="M13.468 12.37C12.758 11.226 11.195 10 8 10s-4.757 1.225-5.468 2.37A6.987 6.987 0 0 0 8 15a6.987 6.987 0 0 0 5.468-2.63z"/>\n' +
            '             <path fill-rule="evenodd" d="M8 9a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>\n' +
            '             <path fill-rule="evenodd" d="M8 1a7 7 0 1 0 0 14A7 7 0 0 0 8 1zM0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8z"/>\n' +
            '        </svg>\n' +
            '        <div class="media-body pb-1 px-3 mb-0 small lh-125 border-bottom border-gray">\n' +
            '             <div class="ratings-container mb-1">\n' +
            '                  <div class="ratings">\n' +
            '                       <div class="ratings-val" style="\'width:'+review.rating * 20+'%;"></div><!-- End .ratings-val -->\n' +
            '                  </div>\n' +
            '                  <span class="ratings-text">'+review.rating+'</span>\n' +
            '               </div>\n' +
            '               <div class="px-3 row">\n' +
            '                    <strong class="d-block text-gray-dark mr-2">'+review.creator.nickName+'</strong>\n' +
            '                            <div>'+convertLocalDateTime(review.createdDate)+'</div>\n' +
            '               </div>\n' +
            '               <p>'+review.contents+'</p>\n' +
            '         </div>\n' +
            '    </div>'
    });

    $('#review > h4').empty();
    $('#review > h4').after(str);

}


function setReviewPageList(pageMaker) {
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

    $('.reviewPagination').html(str);

}


$(".reviewPagination").on("click","li a", function (event){
    event.preventDefault();
    var page = event.target.getAttribute('href');
    getReviewList(page);
});

