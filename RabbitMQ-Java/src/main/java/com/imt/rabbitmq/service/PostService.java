package com.imt.rabbitmq.service;

import com.imt.rabbitmq.beans.Post;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
@AllArgsConstructor
public class PostService {

  @Autowired
  JdbcTemplate jdbcTemplate;

  //Logger logger;

  //// Retrieves a post by its ID from the database.
  //public Post getPostById(long id) {
  //  // Execute SQL query to find the post with the given ID.
  //  return jdbcTemplate.execute((Connection conn) -> {
  //    try (var ps = conn.prepareStatement("SELECT * FROM posts WHERE id = ?")) {
  //      ps.setObject(1, id);
  //      try (var rs = ps.executeQuery()) {
  //        if (rs.next()) {
  //          // Create and return a Post object from the ResultSet.
  //          return new Post(
  //            rs.getString("sub_name"),
  //            rs.getString("title"),
  //            rs.getInt("upvotes"),
  //            rs.getInt("downvotes"),
  //            rs.getInt("num_comments"),
  //            rs.getLong("created_utc"));
  //        }
  //        throw new RuntimeException("post not found by id.");
  //      }
  //    }
  //  });
  //}
  //
  //// Retrieves a list of posts by title.
  //public List<Post> getPostsByTitle(String title) {
  //  // Query the database for posts with the specified title.
  //  return jdbcTemplate.query("SELECT * FROM posts WHERE title = ?", new BeanPropertyRowMapper<>(Post.class), title);
  //}
  //
  // Counts the total number of posts in the database.
  public long getPostsCount() {
    // Execute a count query and return the result.
    long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM POSTS", (ResultSet rs, int rowNum) -> rs.getLong(1));
    //logger.info(count + "");
    return count;
  }

  // Retrieves a paginated list of posts.

  //public List<Post> getPosts(int pageIndex) {
  //  // Calculate the offset and limit for pagination.
  //  int limit = 100;
  //  int offset = limit * (pageIndex - 1);
  //  // Query the database for a page of posts.
  //  return jdbcTemplate.query("SELECT * FROM posts LIMIT ? OFFSET ?", new BeanPropertyRowMapper<>(Post.class), limit, offset);
  //}

  // Adds a new post to the database.
  public Post createPost(String subName, String title, int upvotes, int downvotes, int numComments, long createdUtc) {
    KeyHolder holder = new GeneratedKeyHolder();
    // Execute an insert statement to add the new post.
    if (1 != jdbcTemplate.update((conn) -> {
      var ps = conn.prepareStatement("INSERT INTO posts(sub_name, title, upvotes, downvotes, num_comments, created_utc) VALUES(?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setObject(1, subName);
      ps.setObject(2, title);
      ps.setObject(3, upvotes);
      ps.setObject(4, downvotes);
      ps.setObject(5, numComments);
      ps.setObject(6, createdUtc);
      return ps;
    }, holder)) {
      throw new RuntimeException("Insert failed.");
    }
    // Return the newly created Post object.
    return new Post(subName, title, upvotes, downvotes, numComments, createdUtc);
  }

  //// Updates an existing post in the database.
  //public void updatePost(Post post) {
  //  // Execute an update statement to modify the post.
  //  if (1 != jdbcTemplate.update("UPDATE posts SET title = ?, upvotes = ?, downvotes = ?, num_comments = ?, created_utc = ? WHERE id = ?", post.getTitle(), post.getUpvotes(), post.getDownvotes(), post.getNumComments(), post.getCreatedUtc(), post.getId())) {
  //    throw new RuntimeException("Post not found by id");
  //  }
  //}
}
