package com.imt.persistent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imt.persistent.beans.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * @className: com.imt.persistent.Utils
 * @description: TODO
 * @author: Wenjie FU
 * @date: 05/01/2024
 **/
public class Utils {
  public static List<Post> extractPosts(String jsonStr) {
    List<Post> posts = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();

    try {
      JsonNode rootNode = mapper.readTree(jsonStr);
      JsonNode postsNode = rootNode.get("posts");

      if (postsNode.isArray()) {
        for (JsonNode postNode : postsNode) {
          Post post = mapper.treeToValue(postNode, Post.class);
          post.setSub_name(rootNode.get("sub_name").asText());
          posts.add(post);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return posts;
  }
}
