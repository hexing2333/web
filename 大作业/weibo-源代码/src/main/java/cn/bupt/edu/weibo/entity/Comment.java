package cn.bupt.edu.weibo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
//@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long weiboId;
    private String username;
    private String content;
    private String dataString;
    private Date date;
}
