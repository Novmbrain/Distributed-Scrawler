package com.imt.persistent.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


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
  private String title;
  private int upvotes;
  private int downvotes;
  private int num_comments;
  private long created_utc;
}
