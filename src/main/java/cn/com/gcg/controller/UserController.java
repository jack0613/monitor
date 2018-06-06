package cn.com.gcg.controller;

import cn.com.gcg.dao.UserRepository;
import cn.com.gcg.model.User;
import cn.com.gcg.protocol.MonitorResponse;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 2018/6/4.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @RequestMapping("getAll")
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @RequestMapping(value = "/login/{username}",method = RequestMethod.POST)
    public Object login(@PathVariable("username") String username, String password, HttpServletRequest request){
        //password = request.getParameter("password");
        request.getParameterMap()
        User user = userRepository.findByLoginNameAndLoginPwd(username,password);

        Map<String,Object> map = new HashMap<String,Object>();
        if(user ==null){
            map.put("flag","failed");
        }else{
            request.getSession().setAttribute("loginUser",user);
            map.put("flag","success");
        }
        return map;
    }

//    @RequestMapping(value = "/login",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
//    public MonitorResponse<?> login(@RequestParam( value = "username") String username,@RequestParam(value = "password") String password){
//        return MonitorResponse.success(username + password);
//    }

}
