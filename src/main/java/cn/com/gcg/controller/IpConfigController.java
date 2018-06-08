package cn.com.gcg.controller;

import cn.com.gcg.dao.IpConfigRepository;
import cn.com.gcg.model.IpConfig;
import cn.com.gcg.model.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
public class IpConfigController {
    @Autowired
    private IpConfigRepository ipConfigRepository;

    /**
     * 查询所有用户信息
     * @param ipConfig
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/ip/iplist")
    public Object ipList(final IpConfig ipConfig, @RequestParam(defaultValue = "1") Integer page , @RequestParam(defaultValue = "10") Integer size){

        page = page-1;
        Specification<IpConfig> specification = new Specification<IpConfig>() {
            @Override
            public Predicate toPredicate(Root<IpConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                List<Predicate> predicates = new ArrayList<Predicate>();
                if(ipConfig!=null){
                    if(ipConfig.getIpAddr() !=null && !"".equals(ipConfig.getIpAddr())){
                       // Predicate predicate = cb.equal(root.get("ipAddr"),ipConfig.getIpAddr());
                        Predicate predicate = cb.like(root.get("ipAddr").as(String.class),"%"+ipConfig.getIpAddr()+"%");

                        predicates.add(predicate);
                    }
                    if(ipConfig.getIpName() !=null && !"".equals(ipConfig.getIpName())){
                        Predicate predicate = cb.like(root.get("ipName").as(String.class),"%"+ipConfig.getIpName()+"%");

                        predicates.add(predicate);
                    }
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = new PageRequest(page,size, Sort.Direction.ASC,"id");
        Page<IpConfig> pages = ipConfigRepository.findAll(specification, pageable);


        return pages;
    }

    /**
     *
     添加ip信息
     */
    @RequestMapping(value = "/ip/save",method = RequestMethod.POST)
    public Object saveIpConfig(IpConfig ipConfig, HttpServletRequest request){

        //绑定当前登录用户的地区
        User user =(User)request.getSession().getAttribute("loginUser");
        ipConfig.setAreacode(user.getAreacode());

        IpConfig save = ipConfigRepository.save(ipConfig);

        return save;
    }

    /**
     * 根据Id 检索ip信息
     * @param id
     * @return
     */
    @RequestMapping(value="/ip/getOne/{id}")
    public Object getById(@PathVariable("id") Long id){
        IpConfig ipConfig = ipConfigRepository.getOne(id);
        return ipConfig;
    }

    /**
     * 更新ip信息
     * @param ipConfig
     * @return
     */
    @RequestMapping("/ip/update")
    public Object upateIpCofig(IpConfig ipConfig){
        return ipConfigRepository.save(ipConfig);
    }

    /**
        删除IP信息
     */
    @RequestMapping("/ip/delete/{id}")
    public void deleteIpConfig(@PathVariable("id") Long id){
         ipConfigRepository.delete(id);
    }
}
