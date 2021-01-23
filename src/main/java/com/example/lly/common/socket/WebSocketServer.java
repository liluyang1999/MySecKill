package com.example.lly.common.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
@NoArgsConstructor
@ServerEndpoint("/webSocket/{id}")
public class WebSocketServer {

    //线程安全的WebSocket，记录每个对应客户端的服务端和当前在线数量
    private static Map<String, WebSocketServer> socketServers = new ConcurrentSkipListMap<>();

    private String clientId;  //接受客户端的id
    private Session session;  //发送数据的会话

    public static void sendMessage(WebSocketServer server, String msg) {
        logger.info("***********向" + server.getClientId() + "发送消息: " + msg + "**********");
        server.sendNonBlockingMessage(msg);
        logger.info("**********消息发送成功！**********");
    }

    public static void sendMessageToAll(String msg) {
        logger.info("**********群发消息**********");
        for(Map.Entry<String, WebSocketServer> serverEntry: socketServers.entrySet()) {
            serverEntry.getValue().sendNonBlockingMessage(msg);
        }
        logger.info("**********群发完毕**********");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        String msg = jsonObject.get("msg").getAsString();
        String target = jsonObject.get("to").getAsString();
        if("all".equals(target)) {
            //群发
            sendMessageToAll(msg);
        } else {
            //搜索指定的对象发送
            for(Map.Entry<String, WebSocketServer> serverEntry: socketServers.entrySet()) {
                if(target.equals(serverEntry.getValue().getClientId())) {
                    sendMessage(serverEntry.getValue(), msg);
                }
            }
        }
    }

    @OnOpen
    public void onOpen(@PathParam("id") String clientId, Session session) {
        WebSocketServer server = new WebSocketServer();
        server.setClientId(clientId);
        server.setSession(session);
        increaseCurrentOnlineNumber();
        WebSocketServer.socketServers.put(clientId, server);
        try {
            this.sendBlockingMessage("**********建立成功!**********");
        } catch (IOException e){
            logger.error("**********连接建立异常！**********");
        }
        logger.info("**********id: " + clientId + "的WebSocket服务端已建立！**********");
        logger.info("**********当前在线人数: " + currentOnlineNumber);
    }

    @OnClose
    public void onClose() {
        socketServers.remove(this.getClientId());
        try {
            this.session.close();
        } catch (IOException e) {
            logger.error("***********服务端会话关闭异常！**********", e);
        }
        decreaseCurrentOnlineNumber();
        logger.info("**********服务端正常关闭！**********");
        logger.info("**********当前剩余人数: " + currentOnlineNumber + "**********");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("**********消息推送发生错误！**********");
        throwable.printStackTrace();
    }

    //阻塞式发送
    private void sendBlockingMessage(String message) throws IOException {
        this.getSession().getBasicRemote().sendText(message);
    }

    //非阻塞式发送
    private void sendNonBlockingMessage(String message) {
        this.getSession().getAsyncRemote().sendText(message);
    }

    private static int currentOnlineNumber = 0;

    private static synchronized int getCurrentOnlineNumber() {
        return currentOnlineNumber;
    }

    private static synchronized void increaseCurrentOnlineNumber() {
        currentOnlineNumber++;
    }

    private static synchronized void decreaseCurrentOnlineNumber() {
        currentOnlineNumber--;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static Map<String, WebSocketServer> getSocketServerSet() {
        return socketServers;
    }

    public static void setSocketServerSet(Map<String, WebSocketServer> socketServerSet) {
        WebSocketServer.socketServers = socketServerSet;
    }

    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

}
