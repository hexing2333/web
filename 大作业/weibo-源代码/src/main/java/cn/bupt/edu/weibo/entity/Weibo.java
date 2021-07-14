package cn.bupt.edu.weibo.entity;

import javassist.expr.NewArray;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
//@Table(name = "weibo")
public class Weibo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Date date;
    private String username;
    private String dateString;
    private String content;
    private String url;
    private Integer likeNum;
    private Integer commentNum;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "like_user")
    private List<UserInfo> likeUser = new ArrayList<>();
}
