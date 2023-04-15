package com.example.webclientexercise1.api;

import com.example.webclientexercise1.dtos.MyCombinedResult;
import com.example.webclientexercise1.service.MomondoLikeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  MomondoLikeService momondoLikeService;
  public DemoController(MomondoLikeService momondoLikeService){
    this.momondoLikeService=momondoLikeService;
  }

  private final int SLEEP_TIME = 1000*3;



  @GetMapping(value = "/random-string-slow")
  public String slowEndpoint() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return RandomStringUtils.randomAlphanumeric(10);
  }


  @GetMapping(value = "/momondo-like/{name}")
  public MyCombinedResult momondoLike(@PathVariable String name) {
    return momondoLikeService.getCombinedResponse(name);
  }

}
