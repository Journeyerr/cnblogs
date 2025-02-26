package com.cnblog.qrcodeLogIn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AnYuan
 */

@Controller
@Slf4j
public class HomeController {
    
    @GetMapping("/login")
    public ModelAndView login() {
        log.info("用户进入登录页面");
        return new ModelAndView("login");
    }
   
    @GetMapping("/home")
    public ModelAndView home() {
        log.info("用户扫码登录成功");
        return new ModelAndView("home");
    }
    
}
