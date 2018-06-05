package cn.com.gcg.controller;

import cn.com.gcg.protocol.MonitorResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jack on 2018/6/4.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/login",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public MonitorResponse<?> login(@RequestParam( value = "username") String username,@RequestParam(value = "password") String password){
        return MonitorResponse.success(username + password);
    }

}
