package cn.com.gcg.dao;

import cn.com.gcg.model.BussinessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wzs
 * 日志管理dao操作
 */
public interface LogRepository extends JpaRepository<BussinessLog,Long>,JpaSpecificationExecutor<BussinessLog>{
}
