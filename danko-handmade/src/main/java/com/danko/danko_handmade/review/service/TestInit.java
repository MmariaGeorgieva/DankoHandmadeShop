package com.danko.danko_handmade.review.service;

import com.danko.danko_handmade.review.client.ReviewClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class TestInit implements ApplicationRunner {

    private final ReviewClient reviewClient;

    @Autowired
    public TestInit(ReviewClient reviewClient) {
        this.reviewClient = reviewClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ResponseEntity<String> helloMessage = reviewClient.getHelloMessage("Danko");

//        if (!helloMessage.getStatusCode().is2xxSuccessful()) {
//           throw new ...
//        }
        System.out.println(helloMessage.getBody());
    }
}
