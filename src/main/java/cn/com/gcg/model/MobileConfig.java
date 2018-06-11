package cn.com.gcg.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wzs
 * 电话管理实体类
 */
@ToString
@Data
@Table(name="monitor_phone")
@Entity
public class MobileConfig implements Serializable {
    private static final long serialVersionUID = -7395105735755087557L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //电话号码
    @Column(name="phone")
    private String phone;
    //号码名称
    @Column(name="phone_name")
    private String phoneName;
    //是否启用  1 启用  0 不启用
    @Column(name="enabled")
    private Integer enabled;
    //绑定的地区编码
    @Column(name="areacode")
    private String areacode;
    //电话状态
    @Column(name="status")
    private Integer status;
    //最后通话时间
    @Column(name="last_onlinetime")
    private Date lastOnlietime;

}
