package com.sheandsoul.v1update.repository;

import com.sheandsoul.v1update.entities.Subscription;
import com.sheandsoul.v1update.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUser(User user);
}
