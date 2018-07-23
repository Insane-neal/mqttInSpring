package com.quwoqu.mqtt;

import java.lang.annotation.*;

/**
 * @author: qlaall
 * @Date:2018/7/23
 * @Time:0:28
 */

@Target(ElementType.TYPE)// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Retention(RetentionPolicy.RUNTIME)//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented//说明该注解将被包含在javadoc中
public @interface MqttMessageReceiverHandler {
    String topic();
}
