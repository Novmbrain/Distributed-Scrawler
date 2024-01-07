package com.imt.rabbitmq;

import com.imt.rabbitmq.beans.Post;
import com.imt.rabbitmq.service.PostService;
import com.imt.rabbitmq.utils.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class Starter {
  public static void main(String[] args) throws Exception {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    PostService postService = context.getBean(PostService.class);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      List<Post> posts = Utils.extractPosts(message);
      posts.forEach(
        post -> postService.createPost(
          post.getSubName(),
          post.getTitle(),
          post.getUpvotes(),
          post.getDownvotes(),
          post.getNumComments(),
          post.getCreatedUtc())
      );

    };

    Channel channel = MQReceiver.getChannel();
    channel.basicConsume(MQReceiver.RESULTS_QUEUE, true, deliverCallback, consumerTag -> {
    });
  }
}
