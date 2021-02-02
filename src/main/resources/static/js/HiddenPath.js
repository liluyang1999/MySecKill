//发送秒杀链接获取请求
function requestSeckillUrl() {
    $.ajax({
        type : "GET",
        url : "/seckill/getSeckillUrl",
        data : {
            seckillInfoId : $("#seckillInfoId").val(),
            username : $("#userName").val()
        },
        success : function(data) {
            if(data.code === 0) {
                var encodedUrl = data.data;
                executeSeckill(encodedUrl);
            } else {
                layer.msg(data.message);
            }
        },
        error : function () {
            layer.msg("用户端发生异常！");
        }
    })
}

//正式发送秒杀执行请求, 后端校验成功后进行相应的秒杀逻辑
function executeSeckill(encodedUrl) {
    $.ajax({
        type : "POST",
        url : "/seckill/" + encodedUrl,
        data : {
            seckillInfoId : $("#seckillInfoId").val(),
            userId : $("#userId").val()
        },
        success : function(data) {
            if(data.code === 0) {
                //正式发送请求, 执行秒杀
                getSeckillResult($("#seckillInfoId").val())
            } else {
                layer.msg(data.message);
            }
        },
        error : function() {
            layer.msg("用户端发生异常！");
        }
    })
}
//
// function getSeckillResult() {
//     window.location.href =
// }
