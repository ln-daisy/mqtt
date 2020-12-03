package com.mqtt.nettymqtt.client;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 该程序充当客户端，订阅消息
 * @author ln
 * @Description: ${todo}
 * @date 2020/12/1
 */
public class MQTTClient {

    //    代理服务器ip地址
    public static final String MQTT_BROKER_HOST = "tcp://127.0.0.1:1883";

    //    客户端唯一标识
    public static final String MQTT_CLIENT_ID = "android_xiasuhuei321";

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "password";

    public static final String TOPIC_FILTER = "testTopic";

    private volatile static MqttClient client;

    private static MqttConnectOptions options;

    public static void main(String[] args) {
        try {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(MQTT_BROKER_HOST, MQTT_CLIENT_ID, new MemoryPersistence());
//            配置参数信息
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
            // 这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置用户名
            options.setUserName(USERNAME);
            // 设置密码
            options.setPassword(PASSWORD.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 连接
            client.connect(options);
            // 订阅
            client.subscribe(TOPIC_FILTER);

//            设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("connectionLost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("接收消息主题 : " + topic);
                    System.out.println("接收消息Qos : " + message.getQos());
                    System.out.println("接收消息内容 : " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
