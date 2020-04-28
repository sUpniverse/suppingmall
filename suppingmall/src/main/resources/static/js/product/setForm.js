$(document).ready(function() {

    $().on("click","#cart",function() {
        var cartFrom = setCartForm();
        console.log(cartFrom);

        $.ajax({
            url: "/basket",
            action: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(cartFrom) ,
            success: () => {

            },
            error: () => {

            }
        })

    });

    $(document).on("click","#purchase",function(e) {

        var formObj = $("form[role='form']");

        var orderItemJson = setOrderItemJsonInOrderForm();
        document.getElementById("orderItemJson").setAttribute('value',JSON.stringify(orderItemJson));
        console.log(orderItemJson);
        formObj.attr("action","/orders/orderSheet");
        formObj.attr("method","post");
        formObj.submit();
    });

});

function setOrderItemJsonInOrderForm() {
    var optionItems = document.getElementById('add_option_area').getElementsByTagName('li');
    var Optionlength = optionItems.length;
    var orderItems = [];
    var temp = new Array();
    for(var i = 0; i < Optionlength; i++) {
        var id = getOptionIdInList(optionItems,i);
        var price = getPriceInList(optionItems,i);
        var count = getCountInList(optionItems,i);
        var orderItem = {
            productOption : {
                optionId : id,
            },
            price : price,
            count : count
        };
        temp[id] = orderItem;
    }
    for(var i = 0; i < temp.length; i++) {
        if(temp[i] !== undefined) {
            orderItems.push(temp[i]);
        }
    }
    return orderItems;
}

function setCartForm() {
    var productId = [[${product.productId}]];
    var userId = [[${session.user?.userId}]];
    var cartItems = document.getElementById('add_option_area').getElementsByTagName('li');
    var optionLength = cartItems.length;
    var cartForm = {
        productId : productId,
        userId : userId,
        basketItems : []
    };
    var temp = new Array();

    for(var i = 0; i < optionLength; i++) {
        var id = getOptionIdInList(cartItems,i);
        var price = getPriceInList(cartItems,i);
        var count = getCountInList(cartItems,i);
        var cartItem = {
            productOption : {
                optionId : id,
            },
            price : price,
            count : count
        };
        temp[id] = cartItem;
    }
    for(var i = 0; i < temp.length; i++) {
        if(temp[i] !== undefined) {
            cartForm.basketItems.push(temp[i]);
        }
    }
    return cartForm;


}

// list형태로 된 구매 목록에서 개별 optionId를 가지고 옴
function getOptionIdInList(cartItems,index) {
    return cartItems[index].getAttribute('data-prdno');
}

// list형태로 된 구매 목록에서 개별 가격을 가지고 옴
function getPriceInList(cartItems,index) {
    return parseInt(RemoveCommas(cartItems[index].getElementsByTagName('strong').item(0).textContent));
}

// list형태로 된 구매 목록에서 개별 수량을 가지고 옴
function getCountInList(cartItems,index) {
    return parseInt(cartItems[index].getElementsByTagName('input').item(0).value);
}


function getWholePrice() {
    var optionItems = document.getElementById("add_option_area").getElementsByTagName("li");
    var length = optionItems.length;
    var whole_price = 0;
    for(var i = 0; i < length; i++) {
        if(optionItems[i] !== null) {
            var price = parseInt(RemoveCommas(optionItems[i].getElementsByTagName('strong').item(0).textContent));
            whole_price += price;
        }
    }
    return whole_price;
}

function getWholeCount() {
    var optionItems = document.getElementById("add_option_area").getElementsByTagName("li");
    var length = optionItems.length;
    var whole_count = 0;
    for(var i = 0; i < length; i++) {
        if(optionItems[i] !== null) {
            var count = parseInt(optionItems[i].getElementsByTagName('input').item(0).value);
            whole_count += count;
        }
    }
    return whole_count;
}

// 콤마를 제거후 물품의 가격 구해오기
function RemoveCommas(x) {
    return x.toString().replace(/,/gi,"")
}