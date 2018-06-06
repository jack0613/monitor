package cn.com.gcg.inteceptor;

import cn.com.gcg.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录拦截器
@Component
public class GlobalInteceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //return super.preHandle(request, response, handler);
        User user = (User)request.getSession().getAttribute("loginUser");
        if(user ==null){
            request.getRequestDispatcher("/htm/login.html").forward(request,response);
            return false;
        }else{
            return true;
        }
    }


}
