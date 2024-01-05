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

      //System.out.println(" [x] Received '" + message + "'");
    };

    Channel channel = MQReceiver.getChannel();
    channel.basicConsume(MQReceiver.RESULTS_QUEUE, true, deliverCallback, consumerTag -> { });

    //userService.register("bob@example.com", "password1", "Bob");
    //userService.register("alice@example.com", "password2", "Alice");
    //User bob = userService.getUserByName("Bob");
    //System.out.println(bob);
    //User tom = userService.register("tom@example.com", "password3", "Tom");
    //System.out.println(tom);
    //System.out.println("Total: " + userService.getUsers());
    //for (User u : userService.getUsers(1)) {
    //  System.out.println(u);
    //}
    //((ConfigurableApplicationContext) context).close();
  }
}
