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
            currentTime = new Date(getJsonData(originalData)).toLocaleString();
        },
        error: function () {
            currentTime = (new Date()).toLocaleString();
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
    return "";
}

function deleteCookie(key) {
    //设置过去时间即可删除
    document.cookie = key + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC';
}


//检测有无当前username, 有就显示欢迎, 没有就存储
function checkCookie() {
    var username = getCookie("username");
    if (username !== "") {
        alert("Welcome again " + username);
    } else {
        username = prompt("Please enter here: ", "");
        if (username !== "" && username !== null) {
            setCookie("username", username, 7);
        }
    }
}


function requestUserInfo(token) {
    $.ajax({
        url: "http://localhost:8080/requestUserInfo",
        method: "post",
        cache: false,
        async: false,
        dataType: "json",
        headers: {
            Accept: "application/json;charset=utf-8",
            Authorization: token
        },
        contentType: "application/json;charset=utf-8"
    });
}


// function requestGoToHome2(url, params) {
//     var temp = document.createElement("form");
//     temp.action = url;
//     temp.method = "post";
//     temp.style.display = "none";
//     for(var x in params) {
//         var opt = document.createElement("textarea");
//         opt.name = x;
//         opt.value = params[x];
//         temp.appendChild(opt);
//     }
//     document.body.appendChild(temp);
//     temp.submit();
//     return temp;
// }


