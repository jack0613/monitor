package cn.com.gcg.dao;

import cn.com.gcg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{
}
