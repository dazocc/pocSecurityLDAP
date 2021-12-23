package br.com.dazo.poc.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("user")
    public String user(){
        return "works!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(){
        return "works!";
    }

    @GetMapping("/notwork")
    public String notwork(){
        return "works!";
    }

}
