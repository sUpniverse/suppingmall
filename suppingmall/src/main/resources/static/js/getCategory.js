//카테고리 1차 선택시 관련된 2차 카테고리 가져오기
$('#category_top').on('change',(e) => {
    var category_id = $("#category_top option:selected").val();
    if(category_id > 0) {
        getCategory(category_id,"middle");
    } else {
        $("#category_middle").find("option").remove();
    }
});

//카테고리 2차 선택시 관련된 3차 카테고리 가져오기
$('#category_middle').on('change',(e) => {
    var category_id = $("#category_middle option:selected").val();
    if(category_id > 0) {
        getCategory(category_id,"bottom");
    } else {
        $("#category_bottom").find("option").remove();
    }
});

function setOption(data,optionName) {
    $(optionName).find("option").remove();
    for(var i = 0; i < data.length; i++) {
        var option = '<option value="'+data[i].id+'">'+data[i].name+'</option>';
        $(optionName).append(option);
    }
}

function getCategory(category_id,place) {
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