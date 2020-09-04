package com.mr.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqMessage {

    /**
     * 注入ampq工具类
     */
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息工具类，指定交换机，routingkey 参数
     * @param exchangeName
     * @param routIngKey
     * @param message
     */
    public void sendMessage(String exchangeName,String routIngKey , Object message){
        // 发送消息
        this.amqpTemplate.convertAndSend(exchangeName,routIngKey,message);
    }

}
