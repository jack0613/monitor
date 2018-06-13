package cn.com.gcg.dao;

import cn.com.gcg.model.IpConfig;
import cn.com.gcg.model.MobileConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wzs
 * 电话管理dao操作
 */
public interface MobileRepository extends JpaRepository<MobileConfig,Long>,JpaSpecificationExecutor<MobileConfig>{

    public List<MobileConfig> findByAreacodeAndEnabled(String areacode,Integer enabled);
}
