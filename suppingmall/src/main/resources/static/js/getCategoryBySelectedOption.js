//카테고리 1차 선택시 관련된 2차 카테고리 가져오기
$('#category_top').on('change',(e) => {
    var category_id = $("#category_top option:selected").val();
    if(category_id > 0) {
        getCategoryBySelectedOption(category_id,"middle");
    } else {
        $("#category_middle").find("option").remove();
    }
});

function getCategoryBySelectedOption(category_id, place) {
    $.ajax({
        type:"GET",
        url:"/category/"+ category_id,
        contentType: 'application/json',
        success: (data) => {
            if(place === "middle") {
                setOption(data,"#category_middle");
            } else if (place === "bottom") {
                setOption(data,"#category_bottom");
            }
        },
        error: () => {
            alert('카테고리를 가져올 수 없습니다.');
        }
    });
}