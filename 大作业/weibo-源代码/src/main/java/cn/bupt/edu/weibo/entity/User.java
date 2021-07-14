package cn.bupt.edu.weibo.entity;

import lombok.Data;
import org.hibernate.usertype.UserType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
//@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date lock;
    private String username;
    private String password;
    private String email;
    private Integer fans;
    private Integer follow;
    private Integer errorTime;

    @OneToMany
    @JoinColumn(name = "follow_user")
    private List<UserInfo> followUser = new ArrayList<>();
}
