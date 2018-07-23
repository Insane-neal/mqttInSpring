package com.quwoqu.mqtt;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.*;
import org.springframework.stereotype.Component;

/**
 * @author: qlaall
 * @Date:2018/7/23
 * @Time:21:05
 */
@MqttMessageReceiverHandler(topic = "qilei-test")
@Component
public class TestReceiverHandler implements MessageHandler ,MessageProducer {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Object payload = message.getPayload();
        System.out.println(payload);
    }

    @Override
    public void setOutputChannel(MessageChannel messageChannel) {

    }

    @Override
    public MessageChannel getOutputChannel() {
        return new DirectChannel();
    }
}
