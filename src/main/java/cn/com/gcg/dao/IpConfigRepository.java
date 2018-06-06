package cn.com.gcg.dao;

import cn.com.gcg.model.IpConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IpConfigRepository extends JpaRepository<IpConfig,Long> ,JpaSpecificationExecutor<IpConfig>{


}
