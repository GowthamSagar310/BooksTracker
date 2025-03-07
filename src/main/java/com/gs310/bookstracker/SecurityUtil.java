package com.gs310.bookstracker;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

// helper class
public class SecurityUtil {

    public static String getUserId(Authentication authentication) {
        String userId = null;
        if (authentication.isAuthenticated()) { // safety check
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User oAuth2User) {
                userId = oAuth2User.getAttribute("mail"); // email
            } else if (principal instanceof UserDetails userDetails) {
                userId = userDetails.getUsername(); // email
            }
        }
        return userId;
    }

}
