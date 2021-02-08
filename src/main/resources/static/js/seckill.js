var URL = {

    executeSeckill: function () {
        return "http://localhost:8080/seckill/executeSeckill";
    },

    requestSeckillUrl: function () {
        return "http://localhost:8080/seckill/requstSeckillUrl";
    },

    requestSeckillInfoInProgressList: function () {
        return "http://localhost:8080/seckill/requestSeckillInfoInProgressList";
    },

    requestSeckillInfoInFutureList: function () {
        return "http://localhost:8080/seckill/requestSeckillInfoInFutureList";
    },

    requestSeckillInfoDetail: function (seckillInfoId) {
        return "http://localhost:8080/seckill/requetSeckillInfoDetail/" + seckillInfoId;
    }

}


//请求seckillInfoList
function requestSeckillInfoInProgressList() {
    var seckillInfoInProgressList = '';
    $.ajax({
        url: URL.requestSeckillInfoInProgressList(),
        method: "get",
        cache: false,
        async: false,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (originalData) {
            seckillInfoInProgressList = getJsonData(originalData);
        },
        error: function () {
            alert("获取seckillInfoInProgressDetail失败");
        }
    });
    return seckillInfoInProgressList;
}

function requestSeckillInfoInFutureList() {
    var seckillInfoInFutureList = "";
    $.ajax({
        url: URL.requestSeckillInfoInFutureList(),
        method: "get",
        cache: false,
        async: false,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (originalData) {
            seckillInfoInFutureList = getJsonData(originalData);
        },
        error: function () {
            alert("获取seckillInfoInFutureList失败");
        }
    });
    return seckillInfoInFutureList;
}


//请求seckillInfo详细信息
function requestSeckillInfoDetail(token, seckillInfoId) {
    var seckillInfo = '';
    $.ajax({
        url: URL.requestSeckillInfoDetail(seckillInfoId),
        method: "get",
        cache: false,
        async: true,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        headers: {
            Authorization: token,
            Accept: "application/json;charset=utf-8"
        },
        success: function (originalData) {
            seckillInfo = getJsonData(originalData);
        },
        error: function () {
            alert("获取seckillInfoDetail失败");
        }
    });
    return seckillInfo;
}

function requestSeckillUrl(token, seckillInfoId) {
//发送秒杀链接获取请求
    $.ajax({
        url: URL.requestSeckillUrl(),
        method: "post",
        contentType: "application/json;charset=utf-8",
        headers: {
            Accept: "application/json;charset=utf-8",
            Authorization: token
        },
        data: {
            seckillInfoId: seckillInfoId
        },
        success: function (data) {

            if (data.code === 0) {
                var encodedUrl = data.data;
                executeSeckill(encodedUrl);
            } else {
                layer.msg(data.message);
            }
        },
        error: function () {
            alert("获取秒杀链接失败");
        }
    })
}

//正式发送秒杀执行请求, 后端校验成功后进行相应的秒杀逻辑, 执行后返回秒杀结果
function executeSeckill(token, encodedUrl) {
    var executedResult = "";
    $.ajax({
        url: URL.executeSeckill(),
        method: "post",
        headers: {
            Accept: "application/json;charset=utf-8",
            Authorization: token
        },
        data: {
            "encodedUrl": encodedUrl
        },
        contentType: "application/json;charset=utf-8",
        success: function (originalData) {
            let code = getJsonCode(originalData);
            let msg = getJsonMsg(originalData);
            executedResult = getJsonData(originalData);
        },
        error: function (originalData) {
            let msg = getJsonMsg(originalData);
            layer.msg(msg);
        }
    });
    return executedResult;
}


// function getSeckillResult(seckillInfoId) {
//     $.ajax({
//
//     });
// }


var seckill = {

    checkIdentification: {
        function(username, password) {

        }
    },

    checkPhone: {
        function(phone) {
            return (phone != NaN && phone.length === 11 && !isNaN(phone));
        }
    },

    URL: {
        currentTime: function () {
            return "/seckill/getCurrentTime";
        },
        stateExposer: function (seckillInfoId) {
            return "/seckill/" + seckillInfoId + "/stateExposer";
        },
        startExecution(seckillInfoId, encodedValue) {
            return "/seckill/" + seckillInfoId + "/" + encodedValue + "/startExecution";
        }
    },

    detail: {
        init: function (params) {
            console.log("得到手机号码");
            var username = $.cookie('username');
            var password = $.cookie('password');

        }
    }

}


