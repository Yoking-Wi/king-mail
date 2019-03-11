package top.sinch.kingmail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket 配置类
 *
 * @author yoking-wi
 * @version 2019年3月10日 19:31:41
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/email") //websocket连接端点
                .setAllowedOrigins("*")  //允许跨域访问
                .withSockJS(); // 可以使用sockjs
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端订阅消息前缀  topic 全局广播消息
        registry.enableSimpleBroker("/topic");
        // 客户端发送消息前缀
        registry.setApplicationDestinationPrefixes("/app");
    }
}
