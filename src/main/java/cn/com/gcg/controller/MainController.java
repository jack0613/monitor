package cn.com.gcg.controller;

import cn.com.gcg.model.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {



    @RequestMapping("/validateLogin")
    public Boolean main(HttpServletRequest request){

       User user= (User)request.getSession().getAttribute("loginUser");

       if(user !=null){
           return true;
       }else{
           return false;
       }
    }
}
