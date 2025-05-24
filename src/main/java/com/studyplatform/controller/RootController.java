package com.studyplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle requests to the root URL
 * This controller serves the web_school_page.html file for the web interface
 */
@Controller
public class RootController {

    /**
     * Handle requests to the root URL
     * @return the name of the view to render (web_school_page.html)
     */
    @GetMapping("/")
    public String index() {
        return "forward:/web_school_page.html";
    }

    /**
     * Handle requests to the login URL
     * @return the name of the view to render (web_school_page.html)
     */
    @GetMapping("/login")
    public String login() {
        return "forward:/web_school_page.html";
    }
}
