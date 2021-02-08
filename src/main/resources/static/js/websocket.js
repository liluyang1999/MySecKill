$(function() {
    socket.init();
});

var urlPath = "websocket://localhost:8080/seckill/";

socket = {
    webSocket: "",
    init : function() {
        //userId
        if("WebSocket" in window) {
            this.webSocket = new WebSocket(urlPath + "webSocket/1");
        } else if("MozWebSocket" in window) {
            this.webSocket = new MozWeSocket(urlPath + "/webSocket/1");
        } else {
            webSocket = new SockJS(urlPath + "sockjs/webSocket");
        }
        this.webSocket.onerror = function(event) {
            alert("WebSocket连接时候出现错误, 请尝试刷新页面!");
        };
        this.webSocket.onopen = function(event) {
            alert("连接成功!");
        };
        this.webSocket.onmessage = function(event) {
            alert(event.data);
        };
    }
}