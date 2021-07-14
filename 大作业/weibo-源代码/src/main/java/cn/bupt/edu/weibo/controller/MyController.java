package cn.bupt.edu.weibo.controller;


import cn.bupt.edu.weibo.dao.CommentRepository;
import cn.bupt.edu.weibo.dao.UserInfoRepository;
import cn.bupt.edu.weibo.dao.UserRepository;
import cn.bupt.edu.weibo.dao.WeiboRepository;
import cn.bupt.edu.weibo.entity.Comment;
import cn.bupt.edu.weibo.entity.User;
import cn.bupt.edu.weibo.entity.UserInfo;
import cn.bupt.edu.weibo.entity.Weibo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MyController {
    private UserRepository userRepository;
    private WeiboRepository weiboRepository;
    private CommentRepository commentRepository;
    private UserInfoRepository userInfoRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Autowired
    public void setWeiboRepository(WeiboRepository weiboRepository){
        this.weiboRepository = weiboRepository;
    }
    @Autowired
    public void setCommentRepository(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    @Autowired
    public void setUserInfoRepository(UserInfoRepository userInfoRepository){
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/my")
    public String myIndex(HttpServletRequest request, Model model){

        HttpSession session=request.getSession();
        String isLogin = (String)session.getAttribute("isLogin");
        String name = request.getParameter("username");
        String username=(String)session.getAttribute("username");
        if (name.equals("1") || name.equals(username)){
            model.addAttribute("isAttention", "0");
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
            List<Weibo> weibos = weiboRepository.findByUsername(username);
            model.addAttribute("weibos", weibos);
        } else {
            User user = userRepository.findByUsername(name);
            model.addAttribute("user", user);
            model.addAttribute("isAttention", "0");
            System.out.println(user.getFollowUser().size());
            for(int i = 0; i<user.getFollowUser().size();i++){
                if(user.getFollowUser().get(i).getUsername().equals(username)){
                    model.addAttribute("isAttention", "1");
                    break;
                }
            }
            List<Weibo> weibos = weiboRepository.findByUsername(name);
            model.addAttribute("weibos", weibos);
        }
        model.addAttribute("isLogin", isLogin);
        return "my";
    }
    @RequestMapping("/my/addComment")
    @ResponseBody
    public String addComment(@RequestParam("content") String content, @RequestParam("id") Long weiboId,
                             HttpServletRequest request){
        String username = (String) request.getSession().getAttribute("username");
        Comment comment = new Comment();
        comment.setContent(content);
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        comment.setDate(date);
        comment.setDataString(ft.format(date));
        comment.setUsername(username);
        comment.setWeiboId(weiboId);
        commentRepository.save(comment);

        Weibo weibo = weiboRepository.findById(weiboId).orElse(null);
        if(weibo != null){
            weibo.getComments().add(comment);
            weibo.setCommentNum(weibo.getCommentNum()+1);
            weiboRepository.save(weibo);
        }
        return "success";
    }
    @RequestMapping("/my/like")
    @ResponseBody
    public String like(@RequestParam("weiboId") Long weiboId, HttpServletRequest request){
        Weibo weibo = weiboRepository.findById(weiboId).orElse(null);
        HttpSession session=request.getSession();
        String username=(String)session.getAttribute("username");
        boolean isFind = false;
        if(weibo != null){
            for(int i = 0; i<weibo.getLikeUser().size();i++){
                if (weibo.getLikeUser().get(i).getUsername().equals(username)) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind){
                UserInfo info = userInfoRepository.findByUsername(username);
                weibo.getLikeUser().add(info);
                weibo.setLikeNum(weibo.getLikeNum()+1);
                weiboRepository.save(weibo);
            } else {
                UserInfo info = userInfoRepository.findByUsername(username);
                weibo.getLikeUser().remove(info);
                weibo.setLikeNum(weibo.getLikeNum()-1);
                weiboRepository.save(weibo);
            }
        }
        return "success";
    }
    @RequestMapping("/my/follow")
    @ResponseBody
    public String follow(@RequestParam("followId") Long followId, HttpServletRequest request){
        User user = userRepository.findById(followId).orElse(null);
        HttpSession session=request.getSession();
        String username=(String)session.getAttribute("username");
        if (user != null){
            for(int i = 0; i<user.getFollowUser().size();i++){
                if(user.getFollowUser().get(i).getUsername().equals(username)){
                    return "error";
                }
            }
            if(username.equals(user.getUsername())){
                return "self";
            } else {
                User my = userRepository.findByUsername(username);
                UserInfo info = userInfoRepository.findByUsername(username);
                user.getFollowUser().add(info);
                user.setFans(user.getFans()+1);
                my.setFollow(my.getFollow()+1);
                userRepository.save(user);
                userRepository.save(my);
                return "success";
            }
        }else {
            return "error";
        }
    }
    @RequestMapping("/my/cancelFollow")
    @ResponseBody
    public String cancelFollow(@RequestParam("followId") Long followId, HttpServletRequest request){
        User user = userRepository.findById(followId).orElse(null);
        HttpSession session=request.getSession();
        String username=(String)session.getAttribute("username");
        if (user != null){
            User my = userRepository.findByUsername(username);
            UserInfo info = userInfoRepository.findByUsername(username);
            user.getFollowUser().remove(info);
            user.setFans(user.getFans()-1);
            my.setFollow(my.getFollow()-1);
            userRepository.save(user);
            userRepository.save(my);
            return "success";
        }else {
            return "error";
        }
    }
}
