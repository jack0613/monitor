package cn.com.gcg.dao;

import cn.com.gcg.model.DirConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Jack on 2018-6-11.
 */
public interface DirConfigRepository extends JpaRepository<DirConfig,Long>,JpaSpecificationExecutor<DirConfig> {

    public List<DirConfig> findByEnabled(Integer enabled);
}
