package cn.bupt.edu.weibo.controller;

import cn.bupt.edu.weibo.dao.CommentRepository;
import cn.bupt.edu.weibo.dao.UserInfoRepository;
import cn.bupt.edu.weibo.dao.UserRepository;
import cn.bupt.edu.weibo.dao.WeiboRepository;
import cn.bupt.edu.weibo.entity.User;
import cn.bupt.edu.weibo.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogonController {
    private UserRepository userRepository;
    private UserInfoRepository userInfoRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Autowired
    public void setUserInfoRepository(UserInfoRepository userInfoRepository){
        this.userInfoRepository = userInfoRepository;
    }

    @RequestMapping("/logon")
    public String logon(){
        return "logon";
    }
    @RequestMapping("/logonCheck")
    @ResponseBody
    public String logonCheck(@RequestParam("username")String username, @RequestParam("password")String password,
                             @RequestParam("email")String email){
        User user = userRepository.findByUsername(username);
        if(user == null){
            if(userRepository.findByEmail(email) == null){
                User item = new User();
                item.setUsername(username);
                item.setEmail(email);
                item.setPassword(password);
                item.setFans(0);
                item.setFollow(0);
                item.setErrorTime(0);
                UserInfo info = new UserInfo();
                info.setUserId(item.getId());
                info.setUsername(item.getUsername());
                userRepository.save(item);
                userInfoRepository.save(info);
                return "success";
            } else {
                return "emailError";
            }
        } else {
            System.out.println("usernameError");
            return "usernameError";
        }
    }
}
