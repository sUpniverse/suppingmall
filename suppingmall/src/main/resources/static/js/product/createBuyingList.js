var optionArray = new Array();

$(document).on('change',"#product_options",() => {

    let id = $("#product_options option").index($("#product_options option:selected"));

    if(optionArray.indexOf(id) !== -1) {
        alert('이미 선택되었습니다.');
        $("#product_options option:first").prop("selected",true);
        return;
    }
    optionArray.push(id);

    let price = $("#product_options option:selected").val();
    let name = $("#product_options option:selected").text();

    var optionUl = document.getElementById('add_option_area');
    var optionLi = document.createElement("li");
    optionLi.setAttribute('class','card mb-3');
    optionLi.setAttribute('data-prdno',id);
    optionLi.innerHTML =
        '<div class="card-body">\n' +
        '    <h5 class="card-title">'+name+'</h5>\n' +
        '<div class="row">\n' +
        '  <div class="col-4">\n' +
        '    <div class="input-group">\n' +
        '      <div class="input-group-prepend">\n' +
        '        <button type="button" class="btn btn-outline-dark btn-sm" id="subtractOption">-</button>\n' +
        '      </div>\n' +
        '      <input type="text" id="cuid_'+id+'" class="form-control text-center" value="1">\n' +
        '      <div class="input-group-append">\n' +
        '        <button type="button" class="btn btn-outline-dark btn-sm" id="plusOption">+</button>\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </div>\n' +
        '<div class="col-6">\n' +
        '    <p class="card-text text-right"><strong class="mr-1">'+numberWithCommas(price)+'</strong>원</p>\n' +
        '</div>\n' +
        '<div class="col-1">\n' +
        ' <button type="button" class="btn btn-secondary btn-sm" id="cancelProduct">x</button>\n' +
        '</div>\n' +
        '  </div>\n' +
        '</div>';

    optionUl.appendChild(optionLi);


    $("#product_options option:first").prop("selected",true);
    getSumPrice();

});


function getSumPrice() {
    var pricePrint = document.getElementById("total_pr");
    var wholePrice = numberWithCommas(getWholePrice());
    if(!document.getElementById("total_price")) {
        pricePrint.innerHTML =
            '<hr>\n' +
            '<div class="row justify-content-between" id="total_price">\n' +
            '<strong class="col-4">총 합계 금액</strong>\n' +
            '<span class="col-5 row">\n' +
            '<h4 class="text-danger  mr-1" id="ui_total_price">'+wholePrice+'</h4>\n' +
            '원\n' +
            '</span>\n' +
            '</div>\n' +
            '<hr>';
    } else {
        document.getElementById("ui_total_price").textContent = wholePrice;
    }
}

function changeButtonValue(e,fuc) {
    var button = e.target;
    var valueButton = button.parentElement.parentElement.getElementsByTagName('input').item(0);
    var value = parseInt(valueButton.value);


    priceTag = button.parentElement.parentElement.parentElement.nextElementSibling.getElementsByTagName('strong').item(0);

    var attributeId = valueButton.getAttribute('id').charAt(5);
    var eachPrice = document.getElementById('product_options')[attributeId].value;

    if(fuc === "sub") {
        if( value > 1) {
            valueButton.value = value - 1;
            priceTag.textContent = numberWithCommas(eachPrice * (value-1));
            getSumPrice();
        }
    } else if (fuc === "add") {
        valueButton.value = value + 1;
        priceTag.textContent = numberWithCommas(eachPrice * (value+1));
        getSumPrice();
    } else if (fuc === "change") {
        priceTag.textContent = eachPrice * value;
        getSumPrice();
    }
}

$(document).on('click',"#subtractOption",(e) => {
    changeButtonValue(e,"sub");

});

$(document).on('click',"#plusOption",(e) => {
    changeButtonValue(e,"add");
});

$(document).on('change',"#cuid_",(e) => {
    changeButtonValue(e,"change");
});

$(document).on('click',"#cancelProduct",(e) =>{
    var button = e.target;
    var parent = button.parentElement.parentElement.parentElement.parentElement;
    var parentId = parseInt(parent.getAttribute('data-prdno'));
    parent.remove();
    getSumPrice(parseInt(button.parentElement.parentElement.getElementsByTagName("strong").item(0).textContent) * -1);
    var number = optionArray.indexOf(parentId);
    optionArray.splice(number,1);

    if(optionArray.length === 0) {
        document.getElementById("total_pr").innerText = '';
    }
});