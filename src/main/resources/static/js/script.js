
$(".toogle").click(function() {
    if($($(this).attr("to-id")).is(":visible")) {
        $($(this).attr("to-id")).css("display","none");
        $($(this).attr("to-id")).removeClass("d-flex");
    }else{
        $($(this).attr("to-id")).css("display","block");
        $($(this).attr("to-id")).addClass("d-flex");
    }
});

const toggleSideBar = () => {
    if($(".sidebar").is(":visible")){
        //close 
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
    }else{
        //show
        $(".content").css("margin-left","20%");
        $(".sidebar").css("display","block");
    }
}

//file uplaod 
$(function() {
var ajaxCall;
    // preventing page from redirecting
    $("html").on("dragover", function(e) {
        e.preventDefault();
        e.stopPropagation();
        $("h1").text("Drag here");
    });

    $("html").on("drop", function(e) { e.preventDefault(); e.stopPropagation(); });

    // Drag enter
    $('.upload-area').on('dragenter', function (e) {
        e.stopPropagation();
        e.preventDefault();
        $("h1").text("Drop");
    });

    // Drag over
    $('.upload-area').on('dragover', function (e) {
        e.stopPropagation();
        e.preventDefault();
        $("h1").text("Drop");
    });

    // Drop
    $('.upload-area').on('drop', function (e) {
        e.stopPropagation();
        e.preventDefault();

        $("h1").text("Upload");

        var file = e.originalEvent.dataTransfer.files;
        var fd = new FormData();

        fd.append('file', file[0]);

        uploadData(fd);
    });

    // Open file selector on div click
    $("#uploadfile").click(function(){
        $("#file").click();
    });

    // file selected
    $("#file").change(function(){
        var fd = new FormData();
         // Read selected files
           var totalfiles = document.getElementById('file').files.length;
           for (var index = 0; index < totalfiles; index++) {
              fd.append("file[]", document.getElementById('file').files[index]);
           }
           console.log(fd)

//        var files = $('#file')[0].files[0];
//        var file = $('#file');
//        console.log(file.length);
//        for(var i=0; i<file.length; i++){
//            console.log(file.files[i]);
////            fd.append('file',$('#file')[i].files[i]);
//        }



        uploadData(fd);
    });
    
    $(document).on('click','.stopvideo', function(e){
	    ajaxCall.abort();
	    console.log("Canceled");
	});
});

// Sending AJAX request and upload file
function uploadData(formdata){
    $(".progress-bar").css("width","0%");
    ajaxCall  = $.ajax({
        url: '/user/drive/files',
        type: 'post',
        xhr: function() {
            var xhr = new window.XMLHttpRequest();

            xhr.upload.addEventListener("progress", function(evt) {
              if (evt.lengthComputable) {
                var percentComplete = evt.loaded / evt.total;
                percentComplete = parseInt(percentComplete * 100);
                $(".p-text").text(percentComplete+"%");
                console.log(percentComplete);
                $(".uploaded_data").text(convertSize(evt.loaded).replace(/\s/g, ''));
                $(".progress-bar").css("width",percentComplete+"%");
                $(".progress-bar").removeClass("bg-success");
                $(".progress-bar").addClass("bg-danger");
                $(".u_data").text(convertSizeMb(evt.loaded) +" / "+ convertSizeMb(evt.total));
                if (percentComplete === 100) {
                    $(".progress-bar").removeClass("bg-danger");
                    $(".progress-bar").addClass("bg-success");
                }

              }
            }, false);

            return xhr;
          },
        data: formdata,
        contentType: false,
        processData: false,
        dataType: 'json',
        success: function(response){
        console.log(response);
//            addThumbnail(response);
        }
    });
}


// Added thumbnail
function addThumbnail(data){
    $("#uploadfile h1").remove(); 
    var len = $("#uploadfile div.thumbnail").length;

    var num = Number(len);
    num = num + 1;

    var name = data.name;
    var size = convertSize(data.size);
    var src = data.src;

    // Creating an thumbnail
    $("#uploadfile").append('<div id="thumbnail_'+num+'" class="thumbnail"></div>');
    $("#thumbnail_"+num).append('<img src="'+src+'" width="100%" height="78%">');
    $("#thumbnail_"+num).append('<span class="size">'+size+'<span>');

}

// Bytes conversion
function convertSize(size) {
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (size == 0) return '0 Byte';
    var i = parseInt(Math.floor(Math.log(size) / Math.log(1024)));
    return Math.round(size / Math.pow(1024, i), 2) + ' ' + sizes[i];
}
function convertSizeMb(size) {
	return  parseFloat(size / Math.pow(1024,2)).toFixed(2)+" MB";
}

$(".collaps-btn").click(function() {
    $("#sidebarMenu").css("width","0px");
    $("main").css("width","100%");
    $(".collaps-btn-remove").css("z-index","9999");
    $(".collaps-btn-remove").show();
})
$(".collaps-btn-remove").click(function() {
    $("#sidebarMenu").css("width","");
    $("main").css("width","");
    $(".collaps-btn-remove").css("z-index","0")
    $(".collaps-btn").show();
})