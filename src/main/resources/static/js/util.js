document.write("<script type='text/javascript' src='/static/js/jquery.min.js' th:src='@{/static/js/jquery.min.js}'></script>");

function postToPage(url, token) {
    var form = $("<form method = 'post'></form>");
    form.attr("action", url);
    var input = $("<input type='hidden' id='authorization' name='authorization' value=" + token + " />");
    form.append(input);
    $("html").append(form);
    form.submit();
}

function getJsonCode(data) {
    let jsonStr = JSON.stringify(data);
    let jsonObject = JSON.parse(jsonStr);
    return jsonObject.code;
}

function getJsonMsg(data) {
    let jsonStr = JSON.stringify(data);
    let jsonObject = JSON.parse(jsonStr);
    return jsonObject.msg;

}

function getJsonData(data) {
    let jsonStr = JSON.stringify(data);
    let jsonObject = JSON.parse(jsonStr);
    return jsonObject.data;
}


function getCurrentTime() {
    var currentTime = new Date().toLocaleString();
    $.ajax({
        url: "http://localhost:8080/getCurrentTime",
        method: "get",
        dataType: "json",
        async: false,
        cache: false,
        success: function (originalData) {
            let code = getJsonCode(originalData);
            let msg = getJsonMsg(originalData);
            let data = getJsonData(originalData);
            if (code === 200) {
                currentTime = new Date(getJsonData(originalData)).toLocaleString();
            } else {
                currentTime = (new Date().toLocaleString());
            }
        },
        error: function () {
            currentTime = '服务器系统错误';
        }
    });
    return currentTime;
}

function setCookie(key, value, expiration) {
    let keyAndValue = key + "=" + value + ";";
    let day = new Date();
    day.setTime(day.getTime() + (expiration * 24 * 60 * 60 * 1000));  //ms
    let expiredTime = "expires=" + day.toLocaleString();
    document.cookie = keyAndValue + " " + expiredTime;
}

function getCookie(key) {
    let name = key + "=";
    let cache = document.cookie.split(";");
    for (var i = 0; i < cache.length; i++) {
        var eachCache = cache[i].trim();
        if (eachCache.indexOf(name) === 0) {
            return eachCache.substring(name.length, eachCache.length);
        }
    }
    return null;
}

function deleteCookie(key) {
    //设置过去时间即可删除
    document.cookie = key + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC';
}


//检测有无当前username, 有就显示欢迎, 没有就存储
function checkCookie(key) {
    let username = getCookie(key);
    return username !== "";
}

function requestUserInfo(token) {
    var result;
    $.ajax({
        url: "http://localhost:8080/requestUserInfo",
        method: "post",
        cache: false,
        async: false,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        headers: {
            Accept: "application/json;charset=utf-8",
            Authorization: token
        },
        success: function (data) {
            result = getJsonData(data);
        },
        error: function (data) {
            let msg = getJsonMsg(data);
            alert(msg);
            result = null;
        }
    });
    return result;
}


function requestLogout() {
    let token = getToken();
    if (token != null) {
        $.ajax({
            url: "http://localhost:8080/requestLogout",
            method: "post",
            cache: false,
            async: false,
            dataType: "json",
            headers: {
                Authorization: token,
                Accept: "application/json;charset=utf-8"
            },
            success: function (data) {
                let msg = getJsonData(data);
                alert(msg);
            }
        });
        removeToken();
    }
    window.location.href = "http://localhost:8080/login_page";
}


function goToLoginPage() {
    window.location.href = "http://localhost:8080/login_page";
}

function goToFailurePage() {
    window.location.href = "http://localhost:8080/login_page"
}

function goToTargetPage(url, token) {
    let tempForm = document.createElement("form");
    tempForm.action = url;
    tempForm.method = "post";
    let inputParam = document.createElement("input");
    inputParam.type = "hidden";
    inputParam.name = "authorization";
    inputParam.value = token;
    tempForm.appendChild(inputParam);
    $(document.body).append(tempForm);
    tempForm.submit();
}


function goToOrderPage(url, token, orderId, type) {
    let tempForm = document.createElement("form");
    tempForm.action = url;
    tempForm.method = "post";
    let tokenParam = document.createElement("input");
    tokenParam.type = "hidden";
    tokenParam.name = "authorization";
    tokenParam.value = token;
    tempForm.appendChild(tokenParam);
    if (type === 1) {
        let seckillInfoParam = document.createElement("input");
        seckillInfoParam.type = "hidden";
        seckillInfoParam.name = "seckillInfoId";
        seckillInfoParam.value = orderId;
        tempForm.appendChild(seckillInfoParam);
    } else {
        let productParam = document.createElement("input");
        productParam.type = "hidden";
        productParam.name = "productId";
        productParam.value = orderId
        tempForm.appendChild(productParam);
    }
    $(document.body).append(tempForm);
    tempForm.submit();
}


function isEmpty(obj) {
    return typeof obj == "undefined" || obj == null || obj === "";
}


function refreshPage() {
    requestRefreshToken(getToken());
    window.location.reload();
}

function getRequestParams() {
    var url = location.search;
    var requestParams = {};
    if (url.indexOf("?") !== -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return requestParams;
}

