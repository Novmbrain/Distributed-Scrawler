package com.imt.persistent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class PostTableInitializer {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostConstruct
  public void init() {
    //System.out.println("hi");
    jdbcTemplate.update("DROP TABLE IF EXISTS posts");
    jdbcTemplate.update(
      "CREATE TABLE IF NOT EXISTS posts (" +
        "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " + // Assuming an ID field for unique identification
        "title VARCHAR(255) NOT NULL, " + // Adjust the VARCHAR size as needed
        "upvotes INTEGER NOT NULL, " +
        "downvotes INTEGER NOT NULL, " +
        "num_comments INTEGER NOT NULL, " +
        "created_utc BIGINT NOT NULL)" // Storing created_utc as BIGINT
    );
  }
}
