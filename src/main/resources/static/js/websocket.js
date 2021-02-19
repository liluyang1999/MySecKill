var urlPath = "ws://localhost:8080/webSocket/";

socket = {
    webSocket: "",
    init: function (username) {
        //自行添加userId
        if ("WebSocket" in window) {
            this.webSocket = new WebSocket(urlPath + username);
        } else {
            alert("不支持websocket");
        }

        this.webSocket.onerror = function (event) {
            alert("WebSocket连接时候出现错误");
        };

        this.webSocket.onopen = function (event) {
            alert("WebSocket连接成功, username=" + username);
        };

        this.webSocket.onmessage = function (event) {
            alert(event.data);
        };
    }
}

function closeWebSocket() {
    this.webSocket.close();
}