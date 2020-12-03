package com.mqtt.nettymqtt.methodsecond;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author ln
 * @Description: ${todo}
 * @date 2020/12/1
 */
public class PushCallback implements MqttCallback {
//    在断开连接时调用
    @Override
    public void connectionLost(Throwable throwable) {
// 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");
    }

//    接收已经预订的发布。
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
// subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
    }

//    接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
//    由 MqttClient.connect 激活此回调。
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
