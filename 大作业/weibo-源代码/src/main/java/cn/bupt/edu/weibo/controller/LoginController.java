package cn.bupt.edu.weibo.controller;

import cn.bupt.edu.weibo.dao.CommentRepository;
import cn.bupt.edu.weibo.dao.UserRepository;
import cn.bupt.edu.weibo.dao.WeiboRepository;
import cn.bupt.edu.weibo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class LoginController {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/loginCheck")
    @ResponseBody
    public String loginCheck(HttpServletRequest request,
                             @RequestParam("username")String username, @RequestParam("password") String password){

        User user = userRepository.findByUsername(username);
        if(user != null){
            if(user.getPassword().equals(password)){
                if(request.getSession().getAttribute("isLogin") != null){
                    return "repetition";
                }
                if(user.getErrorTime() >= 3){
                    Date date = new Date();
                    if(date.getTime() - user.getLock().getTime() < 1000*60*60*60){
                        return "lock";
                    }
                }
                request.getSession().setAttribute("isLogin","1");
                request.getSession().setAttribute("username", username);
                user.setErrorTime(0);
                user.setLock(null);
                userRepository.save(user);
                return "success";
            }else {
                user.setErrorTime(user.getErrorTime() + 1);
                if(user.getErrorTime() >= 3){
                    Date date = new Date();
                    user.setLock(date);
                }
                userRepository.save(user);
                return "passwordError";
            }
        } else {
            return "error";
        }
    }
}
