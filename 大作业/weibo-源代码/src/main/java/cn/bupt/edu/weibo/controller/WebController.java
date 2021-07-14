//package cn.bupt.edu.weibo.controller;
//
//import cn.bupt.edu.weibo.dao.CommentRepository;
//import cn.bupt.edu.weibo.dao.UserRepository;
//import cn.bupt.edu.weibo.dao.WeiboRepository;
//import cn.bupt.edu.weibo.entity.User;
//import org.apache.coyote.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Controller
//public class WebController {
//    private UserRepository userRepository;
//    private WeiboRepository weiboRepository;
//    private CommentRepository commentRepository;
//
//    @Autowired
//    public void setUserRepository(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }
//    @Autowired
//    public void setWeiboRepository(WeiboRepository weiboRepository){
//        this.weiboRepository = weiboRepository;
//    }
//    @Autowired
//    public void setCommentRepository(CommentRepository commentRepository){
//        this.commentRepository = commentRepository;
//    }
//    @RequestMapping("/login")
//    public String login(){
//        return "login";
//    }
//    @RequestMapping("/loginCheck")
//    @ResponseBody
//    public String loginCheck(HttpServletRequest request, HttpServletResponse response,
//                        @RequestParam("username")String username, @RequestParam("password") String password) throws IOException {
//        User user = userRepository.findByUsername(username);
//        if(user != null){
//            if(user.getPassword().equals(password)){
//                request.getSession().setAttribute("isLogin",1);
//                //response.sendRedirect("index/"); //登陆成功，重定向到主页
//                //return "{\"msg\" : \"success\"}";
//                return "success";
//            }else {
//                //return "{\"msg\" : \"passwordError\"}";     //密码错误
//                return "passwordError";
//            }
//        } else {
//            //return "{\"msg\" : \"error\"}";     //用户不存在
//            return "error";
//        }
//    }
//    @RequestMapping("/logon")
//    public String logon(){
//        return "logon";
//    }
//    @RequestMapping("/logonCheck")
//    @ResponseBody
//    public String logonCheck(@RequestParam("username")String username,@RequestParam("password")String password,
//                        @RequestParam("email")String email){
//        User user = userRepository.findByUsername(username);
//        System.out.println("接收成功");
//        if(user == null){
//            if(userRepository.findByEmail(email) == null){
//                User item = new User();
//                item.setUsername(username);
//                item.setEmail(email);
//                item.setPassword(password);
//                userRepository.save(item);
//                //return "{\"msg\" : \"success\"}";
//                return "success";
//            } else {
//                //return "{\"msg\" : \"emailError\"}";
//                return "emailError";
//            }
//        } else {
//            //return "{\"msg\" : \"usernameError\"}";
//            System.out.println("usernameError");
//            return "usernameError";
//        }
//    }
//    @RequestMapping("/index")
//    public String index(){
//        return "index";
//    }
//
//}
