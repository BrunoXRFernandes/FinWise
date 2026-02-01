package com.example.finwise.application.adapters;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {

    @RequestMapping("/register")
    @ResponseBody
    public String registerUser(){
        return "register";
    }
}
