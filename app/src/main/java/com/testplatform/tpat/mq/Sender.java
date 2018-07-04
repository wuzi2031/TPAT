package com.testplatform.tpat.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    public final static String EXCHANGER_NAME = "test_plat_topic";

    public static void publish(String exchange, String routingKey, String msg) {
        Connection connection = MQConnection.getConnection();

        Channel channel = null;
        try {
            channel = connection.createChannel();
            // 声明交换机
            channel.exchangeDeclare(exchange, "topic");


            channel.basicPublish(exchange, routingKey, null, msg.getBytes());

            System.out.println("Send msg" + msg);

            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException, TimeoutException {
        String routingKey = "task1.android.equpment1";
        publish(EXCHANGER_NAME, routingKey, "hello task1.android.equpment1");
    }
}
