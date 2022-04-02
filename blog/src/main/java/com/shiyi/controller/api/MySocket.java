package com.shiyi.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 检测实时在线人数
 *
 * @Version: V1.0
 */

@Component
@ServerEndpoint("/websocket")  //该注解表示该类被声明为一个webSocket终端
public class MySocket {

    private final Logger logger = LoggerFactory.getLogger(MySocket.class);

    /**
     * 初始在线人数
     */
    private static int online_num = 0;
    /**
     * 线程安全的socket集合
     */
    private static CopyOnWriteArraySet<MySocket> webSocketSet = new CopyOnWriteArraySet<MySocket>();
    /**
     * 会话
     */
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        logger.info("有链接加入，当前人数为:{}",getOnline_num());
        synchronized(this.session) {
            this.session.getAsyncRemote().sendText(getOnline_num());
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        logger.info("有链接关闭,当前人数为:{}",getOnline_num());
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        logger.info("来自客户端的消息:{}",message);
        synchronized(this.session) {
            this.session.getAsyncRemote().sendText(getOnline_num());
        }
    }

    public synchronized String getOnline_num(){
        return MySocket.online_num+"";
    }
    public synchronized int subOnlineCount(){
        return MySocket.online_num--;
    }
    public synchronized int addOnlineCount(){
        return MySocket.online_num++;
    }
}
