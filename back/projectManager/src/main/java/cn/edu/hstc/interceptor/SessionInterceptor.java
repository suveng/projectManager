package cn.edu.hstc.interceptor;


import cn.edu.hstc.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Veng Su 1344114844@qq.com
 * @date 2018/3/28 8:16
 */
public class SessionInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
//        String uri = request.getRequestURI();
//        if((uri.indexOf("login")>=0)||(uri.indexOf("sign")>=0)||(uri.indexOf("error")>=0)){
//            return true;
//        }
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("userinfo");
//        if(user!=null){
//            return true;
//        }
        //转发到登录
        request.getRequestDispatcher("/login").forward(request,response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}