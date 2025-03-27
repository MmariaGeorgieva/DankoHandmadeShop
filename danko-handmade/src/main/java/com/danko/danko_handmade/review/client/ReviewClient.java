package com.danko.danko_handmade.review.client;

import com.danko.danko_handmade.review.client.dto.LeaveReview;
import com.danko.danko_handmade.web.dto.ReviewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "review-svc", url = "http://localhost:8081/api/v1/reviews")
public interface ReviewClient {

    @PostMapping
    ResponseEntity<Void> leaveReview(@RequestBody LeaveReview leaveReview);

    @GetMapping("/all")
    List<ReviewDto> getAllReviews();


}
