package cn.com.gcg.controller;

import cn.com.gcg.dao.MobileRepository;
import cn.com.gcg.model.IpConfig;
import cn.com.gcg.model.MobileConfig;
import cn.com.gcg.model.User;
import cn.com.gcg.telbox.Dial;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MobileController {

    @Autowired
    private MobileRepository mobileRepository;

    @RequestMapping("/mobile/mobileList")
    public Object phoneList(final  MobileConfig mobileConfig, @RequestParam(defaultValue = "1") Integer page , @RequestParam(defaultValue = "10") Integer size,
                            HttpServletRequest request){
        page = page-1;

        final User user = (User)request.getSession().getAttribute("loginUser");
        final String areacode = user.getAreacode();
        Specification<MobileConfig> specification = new Specification<MobileConfig>() {
            @Override
            public Predicate toPredicate(Root<MobileConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                List<Predicate> predicates = new ArrayList<Predicate>();
                if(mobileConfig!=null){
                    if(mobileConfig.getPhone() !=null && !"".equals(mobileConfig.getPhone())){
                        Predicate predicate = cb.like(root.get("phone").as(String.class),"%"+mobileConfig.getPhone()+"%");

                        predicates.add(predicate);
                    }
                    if(mobileConfig.getPhoneName() !=null && !"".equals(mobileConfig.getPhoneName())){
                        Predicate predicate = cb.like(root.get("phoneName").as(String.class),"%"+mobileConfig.getPhoneName()+"%");

                        predicates.add(predicate);
                    }

                    Predicate predicate = cb.equal(root.get("areacode").as(String.class),areacode);
                    predicates.add(predicate);


                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = new PageRequest(page,size, Sort.Direction.ASC,"id");
        Page<MobileConfig> pages = mobileRepository.findAll(specification, pageable);
        return pages;
    }


    @RequestMapping("/mobile/findOne/{id}")
    public Object findOne(@PathVariable("id") Long id){
        return mobileRepository.findOne(id);
    }

    @RequestMapping("/mobile/update")
    public Object update(MobileConfig mobileConfig){

        MobileConfig mc = mobileRepository.findOne(mobileConfig.getId());
        mc.setEnabled(mobileConfig.getEnabled());
        mc.setPhoneName(mobileConfig.getPhoneName());
        mc.setPhone(mobileConfig.getPhone());
        return mobileRepository.save(mc);
    }

    @RequestMapping("/mobile/save")
    public Object save(MobileConfig mobileConfig,HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("loginUser");
        mobileConfig.setAreacode(user.getAreacode());
        return mobileRepository.save(mobileConfig);
    }

    @RequestMapping("/mobile/delete/{id}")
    public Boolean delete(@PathVariable("id") Long id){

        mobileRepository.delete(id);
        return true;
    }

    @RequestMapping("/mobile/dial")
    public Boolean dialNumber(@RequestParam("phone") String phone){

        //Dial.dial(phone);
        return true;
    }

}
