package com.gs310.bookstracker.account;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ModelAndView root() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")){
            return new ModelAndView("redirect:/home");
        }
        return new ModelAndView("index");
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute UserEntity user) {
        System.out.println("here");
        ModelAndView modelAndView = new ModelAndView("sign-up");
        try {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRole("user");
            UserEntity savedUser = userRepository.save(user);
            if (savedUser.getUserId() > 0) {
                modelAndView.addObject("message", "User registered successfully, redirecting to login page.");
                modelAndView.addObject("success", true);
            } else {
                modelAndView.addObject("message", "User registration failed.");
                modelAndView.addObject("success", false);
            }
            return modelAndView;
        } catch (DataIntegrityViolationException e) {
            String errorMessage = " User registration failed.";
            if (e.getMessage().contains("users_username_key")) {
                errorMessage += " Username is already taken.";
            } else if (e.getMessage().contains("users_email_key")) {
                errorMessage += " Email is already registered.";
            }
            modelAndView.addObject("message", errorMessage);
            modelAndView.addObject("success", false);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("message", "User registration failed.");
            modelAndView.addObject("success", false);
            return modelAndView;
        }
    }



}
