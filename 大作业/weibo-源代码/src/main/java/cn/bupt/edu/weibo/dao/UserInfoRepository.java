package cn.bupt.edu.weibo.dao;

import cn.bupt.edu.weibo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
    UserInfo findByUserId(Long id);
}
