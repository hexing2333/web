package cn.bupt.edu.weibo.json;

import lombok.Data;

import java.util.Date;

@Data
public class WeiboJSON {
    private String dateString;
    private String content;
    private Integer likeNum;
    private Integer comment;
}
