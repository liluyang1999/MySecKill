//发送秒杀链接获取请求
function requestSeckillUrl(seckillInfoId, token) {
    let encodedUrl = null;
    $.ajax({
        url: 'http://localhost:8080/' + seckillInfoId + '/showStateExposer',
        method: "post",
        cache: false,
        async: false,
        headers: {
            Authorization: token,
        },
        success: function (originalData) {
            let code = getJsonCode(originalData);
            let msg = getJsonMsg(originalData);
            let stateExposer = getJsonData(originalData);
            if (code === 200) {
                encodedUrl = stateExposer['encodedUrl'];
                setCookie(encodedUrl);
                window.localStorage.setItem('encodedUrl', encodedUrl);
            } else {
                layer.msg(msg, {icon: 2});
            }
        },
        error: function () {
            alert("获取秒杀链接失败");
        }
    });
    return encodedUrl;
}

//正式发送秒杀执行请求, 后端校验成功后进行相应的秒杀逻辑, 执行后返回秒杀结果
function executeSeckill(seckillInfoId, encodedUrl, token) {
    let executedResult = null;
    $.ajax({
        url: "http://localhost:8080/" + seckillInfoId + "/" + encodedUrl + "/executeSeckillWithAopLock",
        method: "post",
        cache: false,
        async: false,
        headers: {
            Authorization: token
        },
        success: function (originalData) {
            let code = getJsonCode(originalData);
            let msg = getJsonMsg(originalData);
            executedResult = getJsonData(originalData);
        },
        error: function (originalData) {
            executedResult = null;
        }
    });
    return executedResult;
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

const URL = {

    executeSeckill: function () {
        return "http://localhost:8080/seckill/executeSeckill";
    },

    requestSeckillUrl: function () {
        return "http://localhost:8080/seckill/requestSeckillUrl";
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

};


