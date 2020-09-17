//Validation check Point
const emptyReg = /^\s+|\s+$/g;
let nameCheck = false;


const positiveIntegerReg =  /^([1-9])([0-9]{1,9})$/;
let priceCheck = false;
let optionCheck = true;
let deliveryCheck = true;


$(document).on('change',"#product_name",(e) => {
    let name = e.target;
    if(!check(emptyReg,name)) {
        nameCheck = true;
    } else {
        alert("공백은 허용되지 않습니다.");
        name.value = "";
        nameCheck = false;
    }
});



$(document).on('change',"#price",(e) => {
    var price = e.target;
    if(check(positiveIntegerReg,price)) {
        priceCheck = true;
    } else {
        alert("0원이상, 10억미만의 금액만 입력가능 합니다.");
        price.value = "";
        priceCheck = false;
    }
});

const teleReg = /^\d{2,3}-\d{3,4}-\d{4}$/;
let asCheck = false;


$(document).on('change',"#as_number",(e) => {
    let number = e.target;
    if(check(teleReg,number)) {
        asCheck = true;
    } else {
        alert("정확한 번호를 입력하세요.");
        number.value = "";
        asCheck = false;
    }
});


function check(re, what) {
    if(re.test(what.value)) {
        return true;
    }
}

function checkAllValidation() {
    if(nameCheck && priceCheck && optionCheck && asCheck) {
        return true;
    } else {
        if(!nameCheck) {
            document.getElementById('product_name').focus()
        } else if(!priceCheck) {
            document.getElementById('price').focus();
        } else if(!asCheck) {
            document.getElementById('as_number').focus();
        } else if(!optionCheck) {
            document.getElementById('checkOption').focus();
        }
        console.log("name : " + nameCheck + " price : " + priceCheck + " as :" + asCheck + " option : " + optionCheck);
        return false;
    }
}


// 이상하게 정규표현식적용이 되지 않음
$(document).on('click',"#checkOption",() => {
    var count = 0;
    optionCheck = true;

    while(count < $("#option_table tbody tr").length) {
        let name = $("#option_table tbody tr")[count].getElementsByTagName('input')[1].value;
        let price = $("#option_table tbody tr")[count].getElementsByTagName('input')[2].value;
        let quantity = $("#option_table tbody tr")[count].getElementsByTagName('input')[3].value

        if(name.length == 0) {
            alert(count+1 + " 품목 옵션명 에러")
            optionCheck = false;
        } else if(price.length == 0 || check(positiveIntegerReg,price) ) {
            alert(count+1 + " 품목 판매가 에러")
            optionCheck = false;
        } else if(quantity.length == 0 || check(positiveIntegerReg,quantity)) {
            alert(count+1 + " 품목 재고량 에러")
            optionCheck = false;
        }
        count++;
    }
    if(optionCheck)
        alert("확인 완료");
});