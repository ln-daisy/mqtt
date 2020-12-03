package com.mqtt.nettymqtt.methodsecond;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 模拟服务端发送消息
 * 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * @author ln
 * @Description: ${todo}
 * @date 2020/12/1
 */
public class ServerMQTT {

    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://127.0.0.1:18083";
    //定义一个主题
    public static final String TOPIC = "test";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "12345678";

    private MqttClient client;
    private static MqttTopic topic11;
//    private String userName = "mqtt";  //非必须
//    private String passWord = "mqtt";  //非必须

    private static MqttMessage message;

    /**
     * 构造函数
     * @throws MqttException
     */
    public ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    /**
     *  用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
//        options.setUserName(userName);
//        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        options.setAutomaticReconnect(true);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
            topic11 = client.getTopic(TOPIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public static void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);

        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }


    public static void sendMessage(String clieId,String msg)throws Exception{
        ServerMQTT server = new ServerMQTT();
        server.message = new MqttMessage();
        server.message.setQos(1);  //保证消息能到达一次
        server.message.setRetained(true);
        String str ="{\"clieId\":\""+clieId+"\",\"mag\":\""+msg+"\"}";
        server.message.setPayload(str.getBytes());
        try{
            publish(server.topic11 , server.message);
            //断开连接
//            server.client.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     *  启动入口
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws Exception {
        sendMessage("123444","哈哈");
    }
}
