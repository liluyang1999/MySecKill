function getToken() {
    let tokenKey = "token";
    let token = getCookie(tokenKey);
    if (token == null) {
        token = window.localStorage.getItem(tokenKey);
    }
    return token;
}

function setToken(token, days) {
    setCookie("token", token, days);
    window.localStorage.setItem("token", token);
}

function removeToken() {
    deleteCookie("token");
    window.localStorage.removeItem("token");
}

//申请刷新token, 服务端返回后存入cookie中
function requestRefreshToken(token) {
    $.ajax({
        url: "http://localhost:8080/login_page/requestRefreshToken",
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