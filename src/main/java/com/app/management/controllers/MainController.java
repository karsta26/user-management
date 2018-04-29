package com.app.management.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("message", "Welcome!");
        return "index";
    }

    @RequestMapping(value = PATH)
    public String getErrorPage(Model model) {
        model.addAttribute("message", "Something went wrong :(");
        return "index";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
