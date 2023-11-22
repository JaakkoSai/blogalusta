package com.blog.blogalusta.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blog.blogalusta.domain.SignupForm;
import com.blog.blogalusta.domain.BlogUser;
import com.blog.blogalusta.domain.UserRepository;

import jakarta.validation.*;

@Controller
public class UserController {
    @Autowired
    private UserRepository uRepository;

    @RequestMapping(value = "signup")
    public String addStudent(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    /**
     * Create new user
     * Check if user already exists & form validation
     * 
     * @param signupForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
                String pwd = signupForm.getPassword();
                BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
                String hashPwd = bc.encode(pwd);

                BlogUser newUser = new BlogUser();
                newUser.setPasswordHash(hashPwd);
                newUser.setUsername(signupForm.getUsername());
                newUser.setRole("USER");
                newUser.setEmail(signupForm.getEmail());
                if (uRepository.findByUsername(signupForm.getUsername()) == null) {
                    uRepository.save(newUser);
                } else {
                    bindingResult.rejectValue("username", "err.username", "Username already exists");
                    return "signup";
                }
            } else {
                bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");
                return "signup";
            }
        } else {
            return "signup";
        }
        return "redirect:/login";
    }

}
