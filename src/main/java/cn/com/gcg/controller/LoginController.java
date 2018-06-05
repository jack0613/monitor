package cn.com.gcg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login/{loginId}")
    public String login(@PathVariable("loginId") String loginId, String password){





        return "index";
    }

}
