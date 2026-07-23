package com.infinity.scheduler.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a registered user within the Workflow Engine.
 * Contains core identity data and tracks account activity.
 */
public class UserProfile {
    private final String uniqueUserId;
    private String userEmailAddress;
    private String secureHashedPassword;
    private String userFullName;
    private final LocalDateTime accountCreationDate;
    private LocalDateTime lastActiveLoginDate;
    private boolean isAccountActive;

    public UserProfile(String userEmailAddress, String secureHashedPassword, String userFullName) {
        this.uniqueUserId = UUID.randomUUID().toString();
        this.userEmailAddress = userEmailAddress;
        this.secureHashedPassword = secureHashedPassword;
        this.userFullName = userFullName;
        this.accountCreationDate = LocalDateTime.now();
        this.isAccountActive = true;
    }

    // --- Getters and Setters with Validation ---

    public String getUniqueUserId() {
        return uniqueUserId;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        if (userEmailAddress != null && userEmailAddress.contains("@")) {
            this.userEmailAddress = userEmailAddress;
        } else {
            throw new IllegalArgumentException("Invalid email format provided.");
        }
    }

    public String getSecureHashedPassword() {
        return secureHashedPassword;
    }

    public void setSecureHashedPassword(String secureHashedPassword) {
        this.secureHashedPassword = secureHashedPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    public LocalDateTime getLastActiveLoginDate() {
        return lastActiveLoginDate;
    }

    public void updateLastActiveLoginDate() {
        this.lastActiveLoginDate = LocalDateTime.now();
    }

    public boolean getIsAccountActive() {
        return isAccountActive;
    }

    public void setIsAccountActive(boolean isAccountActive) {
        this.isAccountActive = isAccountActive;
    }
}