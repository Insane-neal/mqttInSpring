package com.quwoqu.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: qlaall
 * @Date:2018/7/22
 * @Time:22:41
 */
@Configuration
public class MQTTServerConfig {
    @Value("spring.application.name")
    private String appName;
    @Value("${mqtt.server.url}")
    private String serverUrl;
    @Value("${mqtt.server.port}")
    private String port;
    @Value("${mqtt.server.username:}")
    private String userName;
    @Value("${mqtt.server.password:}")
    private String password;
    @Autowired
    private ApplicationContext context;
    private HashMap<String ,MessageHandler> registredMessageHandlerMap=new HashMap<>();


    @Bean
    public StandardIntegrationFlow mqttInFlow() {
        //获取所有带有MqttMessageHandler注解的类，拼接成String数组，并且注入到Map中去。
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(MqttMessageReceiverHandler.class);
        String[] topics=new String[beansWithAnnotation.size()];
        int i=0;
        for (Map.Entry<String,Object> entry:beansWithAnnotation.entrySet()){
            String topic = entry.getValue().getClass().getAnnotation(MqttMessageReceiverHandler.class).topic();
            topics[i++]=topic;
            registredMessageHandlerMap.putIfAbsent(topic,(MessageHandler) entry.getValue());
        }
        return IntegrationFlows.from(mqttInbound(topics))
                .transform(p -> p)
                .handle(e->{
                    String topic=e.getHeaders().get("mqtt_receivedTopic").toString();
                    registredMessageHandlerMap.get(topic).handleMessage(e);
                })
                .get();
    }
    public MessageProducerSupport mqttInbound(String ... topics) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(appName+"-receiver", mqttClientFactory(), topics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(serverUrl + ":" + port);
        if (!userName.equals("")) {
            factory.setUserName(userName);
        }
        if (!password.equals("")) {
            factory.setPassword(password);
        }
        return factory;
    }


}
