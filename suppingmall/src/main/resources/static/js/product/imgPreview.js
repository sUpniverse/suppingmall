//썸네일 업로드 할 이미지 미리보기
function readURL(input) {
    if (input.files && input.files[0]) {
        resizeThumb(input.files[0]);
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#thumnail_preview').attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}



function resizeThumb(file) {
    const maxWidth = 480;
    var width 	 	= file.width;
    var height 		= file.height;
    var size 		= file.size;

    var ratio = 800/width;

    if(width > maxWidth) {
        width = width * ratio;
        height = height * ratio;
    }
    file.width = width;
    file.height = height;
}