package com.imt.rabbitmq.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @className: Post
 * @description: TODO
 * @author: Wenjie FU
 * @date: 05/01/2024
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
  @JsonProperty(value = "sub_name")
  private String subName;
  private String title;
  private int upvotes;
  private int downvotes;
  @JsonProperty(value = "num_comments")
  private int numComments;
  @JsonProperty(value = "created_utc")
  private long createdUtc;
}
