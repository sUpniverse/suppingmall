//카테고리 1차 선택시 관련된 2차 카테고리 가져오기
$(document).on('click','a.hover',function (e) {
    var category = e.target;
    var id = category.getAttribute('id').toString();
    getCategory(id);
})

function addCategory(data,category_id) {
    var category = document.getElementById(category_id)
}

function setCategory(data) {
    console.log(data)
    document.getElementById('name').textContent = data.name;
    document.getElementById('memo').textContent = data.memo;
}

function getCategory(category_id) {
    $.ajax({
        type:"GET",
        url:"/category/"+ category_id,
        contentType: 'application/json',
        success: (data) => {
            setCategory(data);
        },
        error: () => {
            alert('카테고리를 가져올 수 없습니다.');
        }
    });
}