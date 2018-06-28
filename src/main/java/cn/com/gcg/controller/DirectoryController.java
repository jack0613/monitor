package cn.com.gcg.controller;

import cn.com.gcg.dao.DirConfigRepository;
import cn.com.gcg.model.DirConfig;
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

/**
 * Created by Jack on 2018/6/10.
 */
@RestController
public class DirectoryController {

    @Autowired
    private DirConfigRepository dirConfigRepository;


    /**
     * @author: Jack
     * @data: 2018-6-11 21:54
     * @param: dirconfig page size
     * @description: 目录列表
     * @return：Object
     */
    @RequestMapping("/dir/dirlist")
    public Object dirList(final DirConfig dirConfig, @RequestParam(defaultValue = "1") Integer page , @RequestParam(defaultValue = "10") Integer size,HttpServletRequest request){
        page = page - 1;

        Dial.dial("0,15101139713","\\static\\wav\\ipwarn.wav");

        /*try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("接通状态：" + BriSDKLib.CONNECT_STATUS);*/


        //绑定当前登录用户的地区
        final User user = (User) request.getSession().getAttribute("loginUser");

        Specification<DirConfig> specification = new Specification<DirConfig>() {
            @Override
            public Predicate toPredicate(Root<DirConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<Predicate>();
                if(dirConfig!=null){
                    if(dirConfig.getFilePath() !=null && !"".equals(dirConfig.getFilePath())){
                        Predicate predicate = cb.like(root.get("filePath").as(String.class),"%"+dirConfig.getFilePath()+"%");

                        predicates.add(predicate);
                    }
                    if(dirConfig.getFileName() !=null && !"".equals(dirConfig.getFileName())){
                        Predicate predicate = cb.like(root.get("fileName").as(String.class),"%"+dirConfig.getFileName()+"%");

                        predicates.add(predicate);
                    }
                }
                //添加地区编码条件  根据当前登陆查询
                Predicate predicate = cb.equal(root.get("areacode").as(String.class),user.getAreacode());
                predicates.add(predicate);

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = new PageRequest(page,size, Sort.Direction.ASC,"id");
        Page<DirConfig> pages = dirConfigRepository.findAll(specification, pageable);

        return pages;
    }

    /**
     * @author: Jack
     * @data: 2018-6-11 23:07
     * @param: dirConfig request
     * @description: 目录保存入库
     * @return：Object
     */
    @RequestMapping("/dir/save")
    public Object dirSave(DirConfig dirConfig, HttpServletRequest request){
        //绑定当前登录用户的地区
        User user = (User) request.getSession().getAttribute("loginUser");
        dirConfig.setAreacode(user.getAreacode());

        DirConfig dir = dirConfigRepository.save(dirConfig);

        return dir;
    }

    /**
     * @author: Jack
     * @data: 2018-6-12 21:18
     * @param: id
     * @description: 根据id查询目录信息
     * @return：Object
     */
    @RequestMapping("/dir/getOne/{id}")
    public Object getDirById(@PathVariable("id") Long id){
        DirConfig dir = dirConfigRepository.getOne(id);
        return dir;
    }

    /**
     * @author: Jack
     * @data: 2018-6-12 21:18
     * @param: id
     * @description: 根据id删除目录
     * @return：Object
     */
    @RequestMapping("/dir/delete/{id}")
    public Object deleteDirById(@PathVariable("id") Long id){
        dirConfigRepository.delete(id);
        return true;
    }


    /**
     * @author: Jack
     * @data: 2018-6-12 21:38
     * @param: dirconfig
     * @description: 修改目录信息
     * @return：Object
     */
    @RequestMapping("/dir/update")
    public Object updateDirById(DirConfig dirConfig){
        DirConfig dirConfigNew = dirConfigRepository.getOne(dirConfig.getId());
        DirConfig dir = new DirConfig();
        if(dirConfigNew != null){
            dirConfigNew.setEnabled(dirConfig.getEnabled());
            dirConfigNew.setFileName(dirConfig.getFileName());
            dirConfigNew.setFilePath(dirConfig.getFilePath());
            dirConfigNew.setFileExist(dirConfig.getFileExist());
            dir = dirConfigRepository.save(dirConfigNew);
        }
        return dir;
    }


}
