package com.testplatform.tpat.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {

    public static void subscribe(String exchange, String[] routingKeys) {
        //获取连接
        Connection connection = MQConnection.getConnection();
        //声明队列
        try {
            //从连接中获取频道
            final Channel channel = connection.createChannel();
            // 声明一个随机队列
            String queueName = channel.queueDeclare().getQueue();
//            channel.queueDeclare(queueName, false, false, false, null);
            //绑定队列到交换机 转发器
            for (String routingKey : routingKeys) {
                channel.queueBind(queueName, exchange, routingKey);
            }


            //保证一次只发一个
            channel.basicQos(1);

            DefaultConsumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "utf-8");
                    System.out.println("[1] Recv msg:" + msg);
                    System.out.println("[1] Recv consumerTag:" + consumerTag);
                    System.out.println("[1] Recv RoutingKey:" + envelope.getRoutingKey());
                    System.out.println("[1] Recv Exchange:" + envelope.getExchange());

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("[1] done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            boolean autoAck = false;

            channel.basicConsume(queueName, autoAck, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException, TimeoutException {
        String[] routingKeys = {"task1.#"};
        subscribe(Sender.EXCHANGER_NAME, routingKeys);


    }
}
