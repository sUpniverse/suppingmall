let checkEmailResult = true;
let checkPasswordResult = false;
let checkPhoneResult = true;
let checkAddressResult = true;

const email = getElement('email');
const validation = getElement("validEmail");
const duplication = getElement("duplicateEmail");
const password = getElement('password');
const passwordcheck = getElement('passwordcheck');

function check(re, what) {
    if(re.test(what.value)) {
        return true;
    }
}

function getElement(id) {
    return document.getElementById(id);
}

function getElementValue(id) {
    return document.getElementById(id).value;
}

$(document).on('change keyup paste','#email', function () {
    var reEmail = /^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/;

    checkEmailResult = false;

    if(check(reEmail, email)) {
        validation.className = "valid-feedback";
    } else {
        validation.className = "invalid-feedback";
        duplication.className = "invalid-feedback";
        email.className = "form-control is-invalid";
    }

});

$(document).on('click','#emailCheck',function () {

    if(validation.className === "valid-feedback") {
        var emailval = email.value;
        $.ajax({
            type:"GET",
            url:"/users/emails/"+emailval,
            contentType: 'application/json',
            success: (data) => {
                if(data) {
                    duplication.className = "valid-feedback";
                    email.className = "form-control is-valid";
                    checkEmailResult = true;
                } else {
                    duplication.className = "invalid-feedback";
                    email.className = "form-control is-invalid";
                    alert("아이디가 중복됩니다.");
                }
            }
        });
    } else {
        alert("유효성을 만족하지 않습니다.");
    }
});

$(document).on('change keyup paste','#password', function () {
    var rePassword = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,15}$/;


    if(check(rePassword, password)) {
        password.className = "form-control is-valid";
    } else {
        password.className = "form-control is-invalid";
    }
});

$(document).on('change keyup paste','#passwordcheck', function () {


    if(password.value === passwordcheck.value) {
        passwordcheck.className = "form-control is-valid";
        checkPasswordResult = true;
    } else {
        passwordcheck.className = "form-control is-invalid";
        checkPasswordResult  = false;
    }
});


$(document).on('click',"#verifyPhone",function () {

    var rePhone = /(01[016789])([1-9]{1}[0-9]{2,3})([0-9]{4})$/;


    var first = getElementValue("phoneFirst");
    var middle = getElementValue("phoneMiddle");
    var last = getElementValue("phoneLast");
    var phone = getElement("phone");

    var sum = first + middle + last;

    if (rePhone.test(sum)) {
        alert("인증되었습니다.");
        phone.setAttribute('value',sum);
        checkPhoneResult = true;
    } else {
        alert("인증되지 않았습니다");
    }

});

$(document).on('change',"#phoneFirst",function () {
    checkPhoneResult = false;
});

$(document).on('change',"#phoneMiddle",function () {
    checkPhoneResult = false;
});

$(document).on('change',"#phoneLast",function () {
    checkPhoneResult = false;
});

$(document).on('click',"#search_address",function () {

    var zipcode;
    var address;

    new daum.Postcode({
        popupName: 'postcodePopup',
        theme: {
            searchBgColor: "#0B65C8", //검색창 배경색
            queryTextColor: "#FFFFFF" //검색창 글자색
        },
        autoClose: false,

        oncomplete: function(data) {
            zipcode = data.zonecode;
            if(data.userSelectedType === "R") {
                address = data.roadAddress;
            } else if(data.userSelectedType === "J") {
                address = data.jibunAddress;
            }

            document.getElementById("zip").setAttribute("value",zipcode);
            document.getElementById("address").setAttribute("value",address);

        },

        onclose: function(state) {
            //state는 우편번호 찾기 화면이 어떻게 닫혔는지에 대한 상태 변수 이며, 상세 설명은 아래 목록에서 확인하실 수 있습니다.
            if(state === 'FORCE_CLOSE'){
                //사용자가 브라우저 닫기 버튼을 통해 팝업창을 닫았을 경우, 실행될 코드를 작성하는 부분입니다.
                checkPhoneResult = false;

            } else if(state === 'COMPLETE_CLOSE'){
                //사용자가 검색결과를 선택하여 팝업창이 닫혔을 경우, 실행될 코드를 작성하는 부분입니다.
                //oncomplete 콜백 함수가 실행 완료된 후에 실행됩니다.
                checkPhoneResult = true;
            }
        }
    }).open();
});

function checkAllValidation() {
    if(checkEmailResult && checkPasswordResult && checkPhoneResult && checkAddressResult) {
        return true;
    } else {
        var point;
        if(!checkEmailResult) {
            point = "email";
            email.focus();
        } else if(!checkPasswordResult) {
            point = "password";
            password.focus();
        } else if(!checkPhoneResult) {
            point = "phone";
            document.getElementById("phoneFirst").focus();
        } else if(!checkAddressResult) {
            point = "address";
            document.getElementById("zipcode").focus();
        }
        return false;
    }
}