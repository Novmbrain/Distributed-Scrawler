package com.imt.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MQReceiver {

  public static final String RESULTS_QUEUE = "results_queue";

  public static Channel getChannel() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(RESULTS_QUEUE, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    return channel;
  }
}