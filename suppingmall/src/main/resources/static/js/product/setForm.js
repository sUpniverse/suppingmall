$(document).ready(function() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).on('click',"#cart",function() {
        if(!isSingIn()) {
            return;
        } else if(isSeller()) {
            return ;
        }
        var cartFrom = setCartForm();
        if(cartFrom === undefined) {
            return;
        }

        $.ajax({
            url: "/cart",
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader(header,token);
            },
            data: JSON.stringify(cartFrom) ,
            success: () => {
                var isMove = confirm('장바구니로 이동하시겠습니까?');
                if(isMove) {
                    location.href ="/cart";
                }
            },
            error: () => {

            }
        });

    });

    $(document).on('click',"#purchase",function() {
        if (!isSingIn()) {
            return;
        } else if(isSeller()) {
            return;
        }

        var tempOrder = setOrders();
        if (tempOrder == undefined) {
            alert("선택된 품목이 없습니다.");
            return;
        }

        sendOrderRequest(tempOrder,header,token);

    });

});

function sendOrderRequest(tempOrders,header,token) {
    $.ajax({
        url: "/orders/orderSheet",
        type: 'POST',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type","application/json");
            xhr.setRequestHeader(header,token);
        },
        data: JSON.stringify(tempOrders) ,
        success: (data, status, xhr) => {
            location.href = xhr.getResponseHeader('Location');
        },
        error: () => {

        }
    });
}

function setOrders() {
    var optionItems = document.getElementById('add_option_area').getElementsByTagName('li');
    var optionLength = optionItems.length;

    if(optionLength === 0) {
        return;
    }


    var buyerId = document.getElementById('userId').value;
    var sellerId = document.getElementById('sellerId').value;
    var productId = document.getElementById('productId').value;

    var tempOrderFormList = new Array();

    var tempOrderForm = {
        productId : productId,
        sellerId : sellerId,
        buyerId : buyerId,
        orderItems : []
    }


    var temp = new Array();
    for(var i = 0; i < optionLength; i++) {
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
            tempOrderForm.orderItems.push(temp[i]);
        }
    }

    tempOrderFormList.push(tempOrderForm);

    return tempOrderFormList;
}

function setCartForm() {
    var cartItems = document.getElementById('add_option_area').getElementsByTagName('li');
    var optionLength = cartItems.length;
    var productId = document.getElementById('productId').value;
    var userId = document.getElementById('userId').value;

    if(optionLength == 0) {
        return ;
    }

    var cartForm = {
        productId : productId,
        buyerId : userId,
        cartItemList : []
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
            quantity : count
        };
        temp[id] = cartItem;
    }
    for(var i = 0; i < temp.length; i++) {
        if(temp[i] !== undefined) {
            cartForm.cartItemList.push(temp[i]);
        }
    }
    return cartForm;
}


function isSingIn() {

    if(!document.getElementById("userId")) {
        var Login = confirm('로그인 하시겠습니까?');
        if(Login) {
            location.href = "/users/loginform";
        }
        return false;
    }
    return true;
}

function isSeller() {
    let userId = document.getElementById("userId");
    let sellerId = document.getElementById("sellerId");
    if(userId === sellerId) {
        alert('판매자는 구매할 수 없습니다.');
        return true;
    }
    return false;
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