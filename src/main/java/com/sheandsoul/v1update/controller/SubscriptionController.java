package com.sheandsoul.v1update.controller;

import com.sheandsoul.v1update.dto.StripeCustomerIdRequest;
import com.sheandsoul.v1update.entities.User;
import com.sheandsoul.v1update.services.MyUserDetailService;
import com.sheandsoul.v1update.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private MyUserDetailService userDetailService;

@PostMapping("/start-trial")
    public ResponseEntity<?> startTrial(Authentication authentication, @RequestBody StripeCustomerIdRequest request) {
        String username = authentication.getName();
        User user = userDetailService.findUserByEmail(username);
        subscriptionService.startTrial(user, request.getStripeCustomerId());
        return ResponseEntity.ok("Trial started successfully");
    }

@GetMapping("/status")
    public ResponseEntity<?> getSubscriptionStatus(Authentication authentication) {
        String username = authentication.getName();
        User user = userDetailService.findUserByEmail(username);
        boolean isPremium = subscriptionService.isPremium(user);
        return ResponseEntity.ok(isPremium);
    }
}
