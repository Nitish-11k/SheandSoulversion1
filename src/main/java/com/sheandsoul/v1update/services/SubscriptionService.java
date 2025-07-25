package com.sheandsoul.v1update.services;

import com.sheandsoul.v1update.entities.Subscription;
import com.sheandsoul.v1update.entities.User;
import com.sheandsoul.v1update.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPremium(false);
        return subscriptionRepository.save(subscription);
    }

    public Subscription startTrial(User user, String stripeCustomerId) {
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseGet(() -> createSubscription(user));
        subscription.setPremium(true);
        subscription.setTrialStartDate(LocalDate.now());
        subscription.setTrialEndDate(LocalDate.now().plusDays(15));
        subscription.setStripeCustomerId(stripeCustomerId);
        return subscriptionRepository.save(subscription);
    }

public boolean isPremium(User user) {
        Subscription subscription = subscriptionRepository.findByUser(user).orElse(null);
        if (subscription == null) {
            return false;
        }
        if (subscription.isPremium()) {
            if (subscription.getTrialEndDate() != null && LocalDate.now().isAfter(subscription.getTrialEndDate())) {
                subscription.setPremium(false);
                subscriptionRepository.save(subscription);
                return false;
            }
            return true;
        }
        return false;
    }
}
