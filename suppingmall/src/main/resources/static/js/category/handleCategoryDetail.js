    var name;
    var memo;
$(document).on("click",".btn-warning.update",function() {
    name = document.getElementById('name').textContent;
    memo = document.getElementById('memo').textContent;
    changeCategoryToInput(name,memo);
});

function changeCategoryToInput(name,memo) {
    if(document.getElementById('id').val === undefined) {
        return;
    }
    var categoryNameInput = '<input type="text" name="name" value="'+name+'">';
    document.getElementById('name').innerHTML = categoryNameInput;

    var categoryMemoInput = '<input type="text" name="memo" value="'+memo+'">';
    document.getElementById('memo').innerHTML = categoryMemoInput;

    changeDisplay('detailBtnGrp');
    changeDisplay('changeBtnGrp');
}

function changeDisplay(id) {
    var element =  document.getElementById(id)
    if(element.style.display === "none") {
        element.style.display = "block";
    } else {
        element.style.display = "none";
    }
}

$(document).on("click",".btn-danger.delete",function() {
    var id = document.getElementById('id').val;

    $.ajax({
        type: "DELETE",
        url: "/category/"+id,
        contentType: 'application/json',
        success: (data,status,xhr) => {
            document.getElementById(id).parentElement.innerText = "";
        },
        error: () => {
            alert("카테고리 삭제 실패");
        }
    })

});

$(document).on("click",".btn-primary.cancelDetail",function () {
    initDetail(name,memo);
});

function initDetail(name,memo) {
    document.getElementById('name').innerText = name;
    document.getElementById('memo').innerText = memo;

    changeDisplay('detailBtnGrp');
    changeDisplay('changeBtnGrp');
}



$(document).on("click",".btn-warning.updateDetail",function () {
    var id = document.getElementById('id').val;

    var newName = getInputText('name');
    var newMemo = getInputText('memo');

    if(isEmpty(newName)) {
        alert("이름을 입력해주세요");
        return;
    }
    var category = {
        name : newName,
        memo : newMemo
    };

    $.ajax({
        type: "PUT",
        url: "/category/"+id,
        contentType: 'application/json',
        data: JSON.stringify(category),
        success: (data,status,xhr) => {
            initDetail(newName,newMemo);
        },
        error: () => {
            alert("카테고리 수정 실패");
        }
    })
});

function getInputText(id) {
    return document.getElementById(id).getElementsByTagName('input').item(0).value;
}