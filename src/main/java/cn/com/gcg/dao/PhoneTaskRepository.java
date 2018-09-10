package cn.com.gcg.dao;

import cn.com.gcg.model.PhoneTaskConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jack on 2018-8-1.
 */
public interface PhoneTaskRepository extends JpaRepository<PhoneTaskConfig,Long> {


}
