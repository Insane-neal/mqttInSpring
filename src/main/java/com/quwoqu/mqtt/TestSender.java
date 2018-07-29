package com.quwoqu.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: qlaall
 * @Date:2018/7/26
 * @Time:0:04
 */
@RestController
public class TestSender {
@Autowired
    private IMqttAsyncClient mqttAsyncClient;
@GetMapping("/send")
    public void send(@RequestParam("msg")String msg) throws MqttException {

    MqttMessage mqttMessage = new MqttMessage();
    mqttMessage.setPayload(msg.getBytes());
    IMqttDeliveryToken publish = mqttAsyncClient.publish("qilei-test", mqttMessage);
    publish.waitForCompletion();
    System.err.println("sending over");

}

}

