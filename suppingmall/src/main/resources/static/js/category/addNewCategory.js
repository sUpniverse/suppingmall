$(document).on("click",'#add',function() {
    document.getElementById("addCategoryForm").style.display = 'block';

});

$(document).on("click",'#addCategoryBtn',function () {

    var parentId = 0;
    var name = "";
    var memo = "";
    var enName = "";

    var category = {
        parent : {
            id: parentId
        },
        name : name,
        memo : memo,
        enName : enName
    };
    var bottom_id = parseInt($("#category_bottom option:selected").val());
    if(bottom_id === 0) {
        var middle_id = parseInt($("#category_middle option:selected").val());
        if(middle_id === 0) {
            var top_id = parseInt($("#category_top option:selected").val());
            if(top_id === 0) {
                alert("카테고리를 선택해주세요");
                return;
            }
            category.parent.id = top_id;
        } else {
            category.parent.id = middle_id;
        }
    } else {
        category.parent.id = bottom_id;
    }

    category.name  = document.getElementById('addCategoryName').value;
    category.memo = document.getElementById('addCategoryMemo').value;
    category.enName = document.getElementById('addCategoryEnName').value;
    addCategory(category);
});

$(document).on("click",'#addCategoryCancelBtn',function () {
    document.getElementById('category_top').selectedIndex = 0;
    document.getElementById('category_middle').selectedIndex = 0;
    document.getElementById('addCategoryName').value = "";
    document.getElementById('addCategoryMemo').value = "";
    document.getElementById('addCategoryEnName').value = "";
    document.getElementById("addCategoryForm").style.display = 'none';
});

function addCategory(category) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type:"POST",
        url:"/category",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type","application/json");
            xhr.setRequestHeader(header,token);
        },
        data : JSON.stringify(category),
        success: (data,status,xhr) => {
            var url = xhr.getResponseHeader("Location");
            var id = url.split("/")[4];
            addNewCategoryInList(id,category);

        },
        error: () => {
            alert('카테고리를 추가할 수 없습니다.');
        }
    });

}

function addNewCategoryInList(categoryId, category) {
    var option = '<li class="expandable">' +
        '<a href="#" id="'+categoryId+'" class="">'+category.name+'</a>' +
        '</li>';
    document.getElementById(category.parent.id).parentNode.children[2].innerHTML += option;
    document.getElementById('category_top').selectedIndex = 0;
    document.getElementById('category_middle').selectedIndex = 0;
    document.getElementById('category_bottom').selectedIndex = 0;
    document.getElementById('addCategoryName').value = "";
    document.getElementById('addCategoryMemo').value = "";
    document.getElementById('addCategoryEnName').value = "";
    document.getElementById("addCategoryForm").style.display = 'none';
}

function isEmpty(str) {
    if(str === undefined || str === null || str === "")
        return true;
    else
        return false;
}