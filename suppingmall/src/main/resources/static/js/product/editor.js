$(document).ready(function () {

    let theEditor;
    $("form").submit(function () {
        let images = Array.from( new DOMParser().parseFromString( theEditor.getData(), 'text/html')
            .querySelectorAll( 'img' ) )
            .map( img => img.getAttribute( 'src' ));

        images.forEach(element => console.log(element));
        let urls = document.getElementById('imagesUrl');
        urls.value = images;

        return true;
    });

});


function initEditor(userId,type) {
    ClassicEditor
        .create( document.querySelector('#editor'), {
            language: "ko"
        })
        .then( editor => {
            editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
                return new MyUploadAdapter(loader);
            };
            theEditor = editor;
        })
        .catch(function (error) {
            console.log(error);
        });


    class MyUploadAdapter {

        constructor(loader) {
            this.loader = loader;
        }
        upload() {
            return this.loader.file
                .then(file => new Promise((resolve, reject) => {
                    this._initRequest();
                    this._initListeners(resolve, reject, file);
                    this._sendRequest(file);
                }));
        }

        abort() {
            if (this.xhr) { this.xhr.abort(); }
        }

        _initRequest() {
            const xhr = this.xhr = new XMLHttpRequest();
            xhr.open('POST', '/images/'+type+'/'+userId, true);
            xhr.responseType = 'json';
        }

        _initListeners(resolve, reject, file) {
            const xhr = this.xhr;
            const loader = this.loader;
            const genericErrorText = `Couldn't upload file: ${ file.name }.`;
            xhr.addEventListener('error', () => reject(genericErrorText));
            xhr.addEventListener('abort', () => reject());
            xhr.addEventListener('load', () => {
                const response = xhr.response;
                if (!response || response.error) {
                    return reject(response && response.error ? response.error.message : genericErrorText);
                }
                resolve({
                    default: response.url
                });
            });

            if (xhr.upload) {
                xhr.upload.addEventListener('progress', evt => {
                    if (evt.lengthComputable) {
                        loader.uploadTotal = evt.total;
                        loader.uploaded = evt.loaded;
                    }
                });
            }
        }

        _sendRequest(file) {
            const data = new FormData();
            data.append('file', file);
            this.xhr.send(data);
        }
    }
}