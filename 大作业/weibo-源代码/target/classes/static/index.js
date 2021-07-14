function releaseComment(id){
    var disabled=$("#data").val() ? false : true
    if(disabled==true) {
        alert("请先登录！")
    }
    else {
        var content=$("#comment_content"+id).val()
        $.ajax({
            url: "/addComment",
            type: "POST",
            data: {
                "id":id,
                "content":content
            },
            success: function (result) {
                console.log(result);
                if (result=="success"){
                    var type = $("#type").val()
                    window.location.href="/index?type=" + type
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("错误！");
            }
        })
    }
}
function like(id){
    var disabled=$("#data").val() ? false : true
    if(disabled==true){
        alert("请先登录！")
    }else{
        $.ajax({
            url: "/like",
            type: "POST",
            data: {
                "weiboId":id
            },
            success: function (result) {
                console.log(result);
                if (result=="success"){
                    var type = $("#type").val()
                    window.location.href="/index?type=" + type
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("错误！");
            }
        })
    }

}
function imgPreview(fileDom){
    //判断是否支持FileReader
    if (window.FileReader) {
        var reader = new FileReader();
    } else {
        alert("您的设备不支持图片预览功能，如需该功能请升级您的设备！");
    }

    //获取文件
    var file = fileDom.files[0];
    var imageType = /^image\//;
    //是否是图片
    if (!imageType.test(file.type)) {
        alert("请选择图片！");
        return;
    }
    //读取完成
    reader.onload = function(e) {
        //获取图片dom
        var img = document.getElementById("preview");
        img.style.width="400px";
        img.style.height="400px";
        img.style.objectFit="scale-down"
        //图片路径设置为读取的图片
        img.src = e.target.result;
    };
    reader.readAsDataURL(file);
}