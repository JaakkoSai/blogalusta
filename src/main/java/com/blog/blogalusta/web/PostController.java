package com.blog.blogalusta.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.blog.blogalusta.domain.PostRepository;

@Controller
public class PostController {

    @Autowired
    private PostRepository pRepository;

    @GetMapping(value = { "/index" })
    public String blogiLista(Model model) {
        model.addAttribute("blogit", pRepository.findAll());
        return "index";
    }

}
