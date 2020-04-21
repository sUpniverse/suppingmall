$(document).ready(function() {
    var formObj = $("form[role='form']");
    var productId = [[${product.productId}]];

    $().on("click","#basket",function() {

    });

    $(document).on("click","#purchase",function(e) {

        var orderItemJson = setOrderItemJson();
        document.getElementById("orderItemJson").setAttribute('value',JSON.stringify(orderItemJson));
        console.log(orderItemJson);
        formObj.attr("action","/orders/orderSheet");
        formObj.attr("method","post");
        formObj.submit();
    });

});

function setOrderItemJson() {
    var optionItems = document.getElementById('add_option_area').getElementsByTagName('li');
    var Optionlength = optionItems.length;
    var orderItems = [];
    var temp = new Array();
    for(var i = 0; i < Optionlength; i++) {
        var id = optionItems[i].getAttribute('data-prdno');
        var price = parseInt(RemoveCommas(optionItems[i].getElementsByTagName('strong').item(0).textContent));
        var count = parseInt(optionItems[i].getElementsByTagName('input').item(0).value);
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

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function RemoveCommas(x) {
    return x.toString().replace(/,/gi,"")
}