package cn.com.gcg.controller;

import cn.com.gcg.dao.LogRepository;
import cn.com.gcg.model.BussinessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wzs
 * 日志检索页面
 */
@RestController
public class LogController {

    @Autowired
    private LogRepository logRepository;
    @RequestMapping("/log/logList")
    public Object  findAll(final BussinessLog bussinessLog, @RequestParam(defaultValue = "1") Integer page , @RequestParam(defaultValue = "10") Integer size){

        //jpa 起始页是0开始
        page = page-1;
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"createtime");
        Specification<BussinessLog> specification = new Specification<BussinessLog>() {
            @Override
            public Predicate toPredicate(Root<BussinessLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<Predicate>();
                if(bussinessLog.getContent() !=null && !"".equals(bussinessLog.getContent())){
                    Predicate predicate = cb.like(root.get("content").as(String.class), "%" + bussinessLog.getContent() + "%");
                    predicates.add(predicate);
                }
                if(bussinessLog.getType() !=null && !"".equals(bussinessLog.getType())){
                    cb.equal(root.get("type").as(Integer.class),bussinessLog.getType());
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };


        Page<BussinessLog> logs = logRepository.findAll(specification, pageable);
        return logs;
    }
}
