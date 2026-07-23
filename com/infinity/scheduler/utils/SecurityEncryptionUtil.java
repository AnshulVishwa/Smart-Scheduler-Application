package com.infinity.scheduler.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class responsible for handling cryptographic operations.
 * Provides methods for securely hashing user passwords.
 */
public class SecurityEncryptionUtil {

    /**
     * Hashes a plain text password using SHA-256 encryption.
     * 
     * @param plainTextPassword The raw password provided by the user.
     * @return A Base64 encoded hashed string.
     */
    public static String encryptUserPassword(String plainTextPassword) {
        try {
            MessageDigest cryptographicDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = cryptographicDigest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException hashingException) {
            System.err.println("❌ Error: Encryption algorithm not found.");
            throw new RuntimeException("Failed to hash password securely.", hashingException);
        }
    }

    /**
     * Verifies if a raw password matches a stored hash.
     * 
     * @param rawPassword          Input password to check.
     * @param storedHashedPassword The previously encrypted password from the
     *                             database.
     * @return True if they match, false otherwise.
     */
    public static boolean verifyPasswordMatch(String rawPassword, String storedHashedPassword) {
        String newlyHashedPassword = encryptUserPassword(rawPassword);
        return newlyHashedPassword.equals(storedHashedPassword);
    }
}