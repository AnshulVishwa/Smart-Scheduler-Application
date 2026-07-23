package com.infinity.scheduler.services;

import com.infinity.scheduler.models.UserProfile;
import com.infinity.scheduler.exceptions.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for managing user sessions and registration.
 * Simulates a database using an in-memory HashMap for rapid data access.
 */
public class UserAuthenticationService {

    // In-memory mock database for users. Key = Email, Value = UserProfile
    private final Map<String, UserProfile> userDatabaseMock;
    private UserProfile currentlyLoggedInUser;

    public UserAuthenticationService() {
        this.userDatabaseMock = new HashMap<>();
        this.currentlyLoggedInUser = null;
    }

    /**
     * Registers a new user into the system.
     */
    public void registerNewUser(String emailAddress, String rawPassword, String fullName)
            throws AuthenticationException {
        if (userDatabaseMock.containsKey(emailAddress)) {
            throw new AuthenticationException("Registration failed: A user with this email already exists.");
        }

        // In a real application, we would hash the password here before saving.
        String simulatedHashedPassword = rawPassword + "_hashed";

        UserProfile newUser = new UserProfile(emailAddress, simulatedHashedPassword, fullName);
        userDatabaseMock.put(emailAddress, newUser);
        System.out.println("✅ User successfully registered: " + fullName);
    }

    /**
     * Authenticates a user based on email and password.
     */
    public boolean loginUser(String emailAddress, String rawPassword) throws AuthenticationException {
        if (!userDatabaseMock.containsKey(emailAddress)) {
            throw new AuthenticationException("Login failed: User account not found.");
        }

        UserProfile retrievedUser = userDatabaseMock.get(emailAddress);
        String expectedPasswordHash = rawPassword + "_hashed"; // Matching the simulated hash

        if (retrievedUser.getSecureHashedPassword().equals(expectedPasswordHash)) {
            if (!retrievedUser.getIsAccountActive()) {
                throw new AuthenticationException("Login failed: Account is disabled.");
            }

            this.currentlyLoggedInUser = retrievedUser;
            retrievedUser.updateLastActiveLoginDate();
            System.out.println("🔓 Login successful. Welcome back, " + retrievedUser.getUserFullName());
            return true;
        } else {
            throw new AuthenticationException("Login failed: Incorrect password provided.");
        }
    }

    /**
     * Logs out the current active user.
     */
    public void logoutCurrentUser() {
        if (this.currentlyLoggedInUser != null) {
            System.out.println("🔒 User " + this.currentlyLoggedInUser.getUserFullName() + " logged out.");
            this.currentlyLoggedInUser = null;
        }
    }

    public UserProfile getCurrentlyLoggedInUser() {
        return currentlyLoggedInUser;
    }
}