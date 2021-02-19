package com.example.lly.module.socket;

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
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@NoArgsConstructor
@ServerEndpoint("/webSocket/{username}")
public class WebSocketServer {

    //线程安全的WebSocket，记录每个对应客户端的服务端和当前在线数量
    private static CopyOnWriteArraySet<WebSocketServer> socketServers = new CopyOnWriteArraySet<>();

    private String clientId;  //接受客户端的id, 这里使用账号
    private Session session;  //发送数据的会话


    public static void sendMessage(String username, String msg) {
        for (WebSocketServer socketServer : socketServers) {
            if (username.equals(socketServer.getClientId())) {
                logger.info("WebSocket向" + username + "发送消息: " + msg);
                socketServer.sendNonBlockingMessage(msg);
                logger.info("消息发送成功");
            }
        }
    }


    public static void sendMessageToAll(String msg) {
        logger.info("群发消息");
        for (WebSocketServer server : socketServers) {
            server.sendNonBlockingMessage(msg);
        }
        logger.info("群发完毕");
    }

    public static CopyOnWriteArraySet<WebSocketServer> getSocketServers() {
        return socketServers;
    }

    public static void setSocketServers(CopyOnWriteArraySet<WebSocketServer> socketServers) {
        WebSocketServer.socketServers = socketServers;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        String msg = jsonObject.get("msg").getAsString();
        String target = jsonObject.get("to").getAsString();
        if ("all".equals(target)) {
            //群发
            sendMessageToAll(msg);
        } else {
            //搜索指定的对象发送
            for (WebSocketServer socketServer : socketServers) {
                if (target.equals(socketServer.getClientId())) {
                    sendMessage(socketServer.getClientId(), msg);
                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String clientId) {
        WebSocketServer server = new WebSocketServer();
        server.setClientId(clientId);
        server.setSession(session);
        increaseCurrentOnlineNumber();
        WebSocketServer.socketServers.add(server);
        try {
            this.sendBlockingMessage("建立成功");
        } catch (IOException e) {
            logger.error("连接建立异常");
        }
        logger.info("id: " + clientId + "的WebSocket服务端已建立");
        logger.info("当前在线人数: " + currentOnlineNumber);
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

    @OnClose
    public void onClose() {
        System.out.println(this.getClientId());
        socketServers.remove(this);
        decreaseCurrentOnlineNumber();
        logger.info("服务端正常关闭");
        logger.info("当前剩余人数: " + currentOnlineNumber);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("连接发生错误");
//        throwable.printStackTrace();
        session.getAsyncRemote().sendText("Hello World~");
    }

    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

}
