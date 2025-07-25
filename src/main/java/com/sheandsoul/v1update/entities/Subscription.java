package com.sheandsoul.v1update.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean premium;
    private LocalDate trialStartDate;
    private LocalDate trialEndDate;
    private String stripeCustomerId;
	public boolean isPremium() {
		return premium;
	}
	public LocalDate getTrialEndDate() {
		return trialEndDate;
	}
}
