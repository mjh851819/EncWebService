$(document).ready(function () {
    $('#uploadForm').submit(function (event) {

        if ($('#ex_filename').val() == "") {
            event.preventDefault(); // 폼 전송을 방지
            alert("파일을 선택해주세요.");
            return; // 함수 종료
        }

        event.preventDefault();

        // 파일이 선택된 경우에는 폼을 제출하고 모달 표시
        $('#myModal').css('display', 'block');

        var formData = new FormData($(this)[0]);

        $.ajax({
            xhr: function () {
                var xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener('progress', function (e) {
                    if (e.lengthComputable) {
                        var percent = Math.round((e.loaded / e.total) * 100);
                        $('#progressText').text('업로드 중... ' + percent + '%');
                        $('#progressBar').css('width', percent + '%').attr('aria-valuenow', percent);
                    }
                }, false);

                return xhr;
            },
            type: 'POST',
            url: $(this).attr('action'),
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                // 업로드 완료 시 동작
                console.log('업로드 완료');
                // 업로드 모달 닫기
                $('#myModal').css('display', 'none');
                location.reload();
            },
            error: function () {
                // 업로드 실패 시 동작
                console.log('업로드 실패');
                // 업로드 모달 닫기
                $('#myModal').css('display', 'none');
            }
        });
    });
});

$(document).ready(function(){
    var fileTarget = $('.filebox .upload-hidden');

    fileTarget.on('change', function(){  // 값이 변경되면
        if(window.FileReader){  // modern browser
            var filename = $(this)[0].files[0].name;
        }
        else {  // old IE
            var filename = $(this).val().split('/').pop().split('\\').pop();  // 파일명만 추출
        }

        // 추출한 파일명 삽입
        $(this).siblings('.upload-name').val(filename);
    });
});

function goToPreviousPage() {
    var currentPage = parseInt(document.getElementById('currentPage').textContent);
    // currentPage와 totalPage가 null이 아닌 경우에만 비교 수행
    if (currentPage > 1) { // 첫 번째 페이지일 때는 이전 페이지로 이동하지 않음
        window.location.href = '/file?page=' + (currentPage - 1);
    }
}

function goToNextPage() {

    var currentPage = parseInt(document.getElementById('currentPage').textContent);
    var totalPage = parseInt(document.getElementById('totalPage').textContent);
    // 페이지가 로드될 때 currentPage와 totalPage가 null이 아닌지 확인
    if (currentPage != totalPage) { // 첫 번째 페이지일 때는 이전 페이지로 이동하지 않음
        window.location.href = '/file?page=' + (currentPage + 1);
    }


}

function initializePagination() {
    var currentPage = parseInt(document.getElementById('currentPage').textContent);
    var totalPage = parseInt(document.getElementById('totalPage').textContent);
    var previousButton = document.getElementById('previousButton');
    var nextButton = document.getElementById('nextButton');

    // 첫 번째 페이지일 때는 이전 버튼을 비활성화
    if (currentPage === 1) {
        previousButton.disabled = true;
    }

    // 마지막 페이지일 때는 다음 버튼을 비활성화
    if (currentPage === totalPage) {
        nextButton.disabled = true;
    }
}

// DOMContentLoaded 이벤트가 발생하면 initializePagination 함수를 실행
document.addEventListener('DOMContentLoaded', initializePagination);