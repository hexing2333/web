package cn.bupt.edu.weibo.dao;

import cn.bupt.edu.weibo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByWeiboId(Long weiboId);
}
