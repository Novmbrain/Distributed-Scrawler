package com.imt.persistent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @className: PostService
 * @description: TODO
 * @author: Wenjie FU
 * @date: 05/01/2024
 **/
@Component
public class PostService {
  @Autowired
  JdbcTemplate jdbcTemplate;

}
