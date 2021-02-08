// document.write("<script type='text/javascript' src='/static/js/jquery.min.js' th:src='@{/static/js/jquery.min.js}'></script>");
var token = null;

$(document).ready(function () {
    var token = getJwtToken();
    alert("token" + token);
});

function getToken() {
    let tokenKey = "token";
    let token = getCookie(tokenKey);
    if (token == null) {
        token = window.localStorage.getItem(tokenKey);
    }
    return token;
}

function setToken(token) {
    setCookie("token");
    window.localStorage.setItem("token", token);
}

function removeToken() {
    deleteCookie("token");
}

//申请刷新token, 服务端返回后存入cookie中
function refreshToken(token) {
    $.ajax({
        url: "http://localhost:8080/requestRefreshToken",
        method: "get",
        headers: {
            Accept: "application/json;charset=utf-8",
            Authorization: token
        },
        success: function (originalData) {
            let newToken = getJsonData(originalData);
            setToken(newToken);
            alert("token刷新成功");
        },
        error: function () {
            alert("token刷新失败");
        },
        contentType: "application/json;charset=utf-8"
    });
}