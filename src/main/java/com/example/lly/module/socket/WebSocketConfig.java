package com.example.lly.module.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    //秒杀消息推送配置
    @Bean
    public ServerEndpointExporter getServerEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
