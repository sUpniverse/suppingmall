$(document).on("click",'#add',function() {
    document.getElementById("addCategoryForm").style.display = 'block';
});

$(document).on("click",'#addCategoryBtn',function () {

    var parentId = 0;
    var name = "";
    var memo = "";

    var category = {
        parent : {
            id: parentId
        },
        name : name,
        memo : memo
    };

    var middle_id = $("#category_middle option:selected").val();

    if(middle_id === 0) {
        var top_id = $("#category_top option:selected").val();
        if(top_id === 0) {
            alert("카테고리를 선택해주세요");
            return;
        }
        category.parent.id = top_id;
    } else {
        category.parent.id = middle_id;
    }
    category.name  = document.getElementById('addCategoryName').value;
    category.memo = document.getElementById('addCategoryMemo').value;

    addCategory(category);

});

$(document).on("click",'#addCategoryCancelBtn',function () {
    document.getElementById('category_top').selectedIndex = 0;
    document.getElementById('category_middle').selectedIndex = 0;
    document.getElementById('addCategoryName').textContent = "";
    document.getElementById('addCategoryMemo').textContent = "";
    document.getElementById("addCategoryForm").style.display = 'none';
});

function addCategory(category) {

    $.ajax({
        type:"POST",
        url:"/category",
        contentType: 'application/json',
        data : JSON.stringify(category),
        success: (data,status,xhr) => {
            var url = xhr.getResponseHeader("Location");
            var id = url.split("/")[4];
            addNewCategoryInList(id,category);

        },
        error: () => {
            alert('카테고리를 가져올 수 없습니다.');
        }
    });

}

function addNewCategoryInList(categoryId, category) {
    var option = '<li class="expandable">' +
        '<a href="#" id="'+categoryId+'" class="">'+category.name+'</a>' +
        '</li>';
    document.getElementById(category.parent.id).nextSibling.nextSibling.innerHTML += option;
    document.getElementById('addCategoryName').textContent = "";
    document.getElementById('addCategoryMemo').textContent = "";
    document.getElementById("addCategoryForm").style.display = 'none';
}

function isEmpty(str) {
    if(str === undefined || str === null || str === "")
        return true;
    else
        return false;
}