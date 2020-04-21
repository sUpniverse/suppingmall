//썸네일 업로드 할 이미지 미리보기
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#thumnail_preview').attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}