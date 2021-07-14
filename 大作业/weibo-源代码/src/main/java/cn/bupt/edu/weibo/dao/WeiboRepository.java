package cn.bupt.edu.weibo.dao;

import cn.bupt.edu.weibo.entity.Weibo;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeiboRepository extends JpaRepository<Weibo,Long> {
    Page<Weibo> findByUsername(String username, Pageable pageable);
    List<Weibo> findByUsername(String username);
}
