package com.imt.persistent;

import com.imt.persistent.service.User;
import com.imt.persistent.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @className: main
 * @description: TODO
 * @author: Wenjie FU
 * @date: 17/11/2023
 **/
public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    UserService userService = context.getBean(UserService.class);


    userService.register("bob@example.com", "password1", "Bob");
    userService.register("alice@example.com", "password2", "Alice");
    User bob = userService.getUserByName("Bob");
    System.out.println(bob);
    User tom = userService.register("tom@example.com", "password3", "Tom");
    System.out.println(tom);
    System.out.println("Total: " + userService.getUsers());
    for (User u : userService.getUsers(1)) {
      System.out.println(u);
    }
    ((ConfigurableApplicationContext) context).close();
  }
}
