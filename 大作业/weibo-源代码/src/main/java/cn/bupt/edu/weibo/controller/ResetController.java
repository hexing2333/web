package cn.bupt.edu.weibo.controller;

import cn.bupt.edu.weibo.dao.CommentRepository;
import cn.bupt.edu.weibo.dao.UserInfoRepository;
import cn.bupt.edu.weibo.dao.UserRepository;
import cn.bupt.edu.weibo.dao.WeiboRepository;
import cn.bupt.edu.weibo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ResetController {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping("/Reset")
    public  String Reset(){
        return "Reset";
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(@RequestParam("username") String username,
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpServletRequest request){
        User user = userRepository.findByUsername(username);
        if(user!=null){
            if(user.getEmail().equals(email)){
                user.setPassword(password);
                userRepository.save(user);
                if(request.getSession().getAttribute("isLogin") != null){
                    request.getSession().removeAttribute("isLogin");
                }
                if(request.getSession().getAttribute("username") != null){
                    request.getSession().removeAttribute("username");
                }
                return "success";
            }else {
                return "emailError";
            }
        }else {
            return "error";
        }
    }
}
