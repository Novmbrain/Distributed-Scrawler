package com.imt.persistent;

import com.imt.persistent.beans.Post;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @className: main
 * @description: TODO
 * @author: Wenjie FU
 * @date: 17/11/2023
 **/
public class Main {


  public static void main(String[] args) throws Exception {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    //UserService userService = context.getBean(UserService.class);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      List<Post> posts = Utils.extractPosts(message);
      posts.stream().forEach(System.out::println);
    };

    Channel channel = MQReceiver.getChannel();
    channel.basicConsume(MQReceiver.RESULTS_QUEUE, true, deliverCallback, consumerTag -> { });
  }
}
