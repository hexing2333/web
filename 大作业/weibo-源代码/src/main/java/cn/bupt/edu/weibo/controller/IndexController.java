package cn.bupt.edu.weibo.controller;

import cn.bupt.edu.weibo.dao.CommentRepository;
import cn.bupt.edu.weibo.dao.UserInfoRepository;
import cn.bupt.edu.weibo.dao.UserRepository;
import cn.bupt.edu.weibo.dao.WeiboRepository;
import cn.bupt.edu.weibo.entity.Comment;
import cn.bupt.edu.weibo.entity.User;
import cn.bupt.edu.weibo.entity.UserInfo;
import cn.bupt.edu.weibo.entity.Weibo;
import cn.bupt.edu.weibo.json.CommentJSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.ConstantBootstraps;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {
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

    @GetMapping({"","/index"})
    public String index(HttpServletRequest request, Model model, @RequestParam(value = "type", defaultValue = "1") String type,
                        @RequestParam(value = "start", defaultValue = "0") Integer start,
                        @RequestParam(value = "limit", defaultValue = "3") Integer limit){

        HttpSession session=request.getSession();
        String isLogin=(String)session.getAttribute("isLogin");
        String username=(String)session.getAttribute("username");
        model.addAttribute("isLogin",isLogin);
        model.addAttribute("username",username);
        System.out.println("456");
        start = start < 0 ? 0 : start;
        if(type.equals("1")){
            model.addAttribute("type", "1");
            Sort sort = Sort.by(Sort.Direction.DESC, "date");
            Pageable pageable = PageRequest.of(start, limit, sort);
            Page<Weibo> weibos = weiboRepository.findAll(pageable);
            model.addAttribute("weibos", weibos);
        } else if(type.equals("2")){
            model.addAttribute("type", "2");
            Sort sort = Sort.by(Sort.Direction.DESC, "likeNum");
            Pageable pageable = PageRequest.of(start, limit, sort);
            Page<Weibo> weibos = weiboRepository.findAll(pageable);
            model.addAttribute("weibos", weibos);
        } else if(type.equals("3")){
            model.addAttribute("type", "3");
            Sort sort = Sort.by(Sort.Direction.DESC, "commentNum");
            Pageable pageable = PageRequest.of(start, limit, sort);
            Page<Weibo> weibos = weiboRepository.findAll(pageable);
            model.addAttribute("weibos", weibos);
        }
        System.out.println("123");
        return "index";
    }
    @PostMapping({"","/index"})
    public String index1(HttpServletRequest request, Model model, @RequestParam(value = "type",defaultValue = "1") String type,
                        @RequestParam(value = "start", defaultValue = "0") Integer start,
                        @RequestParam(value = "limit", defaultValue = "3") Integer limit){
        HttpSession session=request.getSession();
        String isLogin=(String)session.getAttribute("isLogin");
        String username=(String)session.getAttribute("username");
        model.addAttribute("isLogin",isLogin);
        model.addAttribute("username",username);
        System.out.println("456");
        start = start < 0 ? 0 : start;
        if(type == null||type.equals("1")){
            System.out.println("1");
            model.addAttribute("type", "1");
            Sort sort = Sort.by(Sort.Direction.DESC, "date");
            Pageable pageable = PageRequest.of(start, limit, sort);
            Page<Weibo> weibos = weiboRepository.findAll(pageable);
            model.addAttribute("weibos", weibos);
        } else if(type.equals("2")){
            model.addAttribute("type", "2");
            Sort sort = Sort.by(Sort.Direction.DESC, "likeNum");
            Pageable pageable = PageRequest.of(start, limit, sort);
            Page<Weibo> weibos = weiboRepository.findAll(pageable);
            model.addAttribute("weibos", weibos);
        } else if(type.equals("3")){
            model.addAttribute("type", "3");
            Sort sort = Sort.by(Sort.Direction.DESC, "commentNum");
            Pageable pageable = PageRequest.of(start, limit, sort);
            Page<Weibo> weibos = weiboRepository.findAll(pageable);
            model.addAttribute("weibos", weibos);
        }
        return "index::weiboList";
    }

    @RequestMapping("/addWeibo")
    @ResponseBody
    public String addWeibo(@RequestParam("content") String content,
                           HttpServletRequest request) throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        User user = userRepository.findByUsername(username);
        Weibo weibo = new Weibo();
        weibo.setUsername(username);
        weibo.setUserId(user.getId());
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        weibo.setDate(date);
        weibo.setDateString(ft.format(date));
        weibo.setContent(content);
        weibo.setLikeNum(0);
        weibo.setCommentNum(0);
        if(request.getSession().getAttribute("url")!=null){
            weibo.setUrl((String)request.getSession().getAttribute("url"));
            request.getSession().removeAttribute("url");
        }
        weiboRepository.save(weibo);
        return "success";
    }
    @PostMapping("/addpic")
    @ResponseBody
    public void addPic(@RequestParam("file") MultipartFile file,
                       HttpServletRequest request) throws IOException {
        String url = getUrl(file);
        request.getSession().setAttribute("url", url);
    }
    //发评论
    @RequestMapping("/addComment")
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

        System.out.println("add");
        Weibo weibo = weiboRepository.findById(weiboId).orElse(null);
        if(weibo != null){
            weibo.getComments().add(comment);
            weibo.setCommentNum(weibo.getCommentNum()+1);
            weiboRepository.save(weibo);
        }
        return "success";
    }
    @RequestMapping("/getComment")
    public String showComments(@RequestParam("id") Long weiboId, Model model){
        System.out.println("789");
        List<Comment> comments = commentRepository.findByWeiboId(weiboId);
        if(!comments.isEmpty()){
            System.out.println(comments);
            model.addAttribute("comments",comments);
            System.out.println("sjdajdl");
        }
        return "redirect:/index";
    }
    @RequestMapping("/deleteWeibo")
    @ResponseBody
    public String deleteWeibo(@RequestParam("weiboId") Long weiboId){
        weiboRepository.deleteById(weiboId);
        return "success";
    }
    @RequestMapping("/like")
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
    @RequestMapping("logout")
    @ResponseBody
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("isLogin");
        request.getSession().removeAttribute("username");

        return "success";
    }
    public String getUrl(MultipartFile fileupload) throws OSSException, ClientException, IOException {
        //填写自己的帐号信息
        String endpoint = "oss-cn-qingdao.aliyuncs.com";
        String accessKeyId = "LTAI4G7aiUKJdsnu72yYEsCV";
        String accessKeySecret = "993lpxZDaPQwXKp2FVLWwEO2rTZTUs";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 文件桶
        String bucketName = "shuoweb";
        // 文件名格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        // 该桶中的文件key
        String dateString = sdf.format(new Date()) + ".jpg";// 20180322010634.jpg
        // 上传文件
        ossClient.putObject("shuoweb", dateString, new ByteArrayInputStream(fileupload.getBytes()));

        // 设置URL过期时间为100年，默认这里是int型，转换为long型即可
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 100);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, dateString, expiration);
        return url.toString();
    }

}
