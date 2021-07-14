package cn.bupt.edu.weibo.json;

import lombok.Data;

import java.util.Date;

@Data
public class CommentJSON {
    private String username;
    private String dateString;
    private String content;
}
