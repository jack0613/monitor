package cn.com.gcg.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wzs
 * 日志管理实体类
 */
@ToString
@Data
@Table(name="monitor_log")
@Entity
public class BussinessLog implements Serializable {

    private static final long serialVersionUID = 3954261357207065916L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //日志内容
    @Column(name="content")
    private String content;
    //日志类型 1 正常日志  2故障日志
    @Column(name="type")
    private Integer type;
    //日志产生时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createtime")
    private Date createtime;
    //所属地区
    @Column(name="areacode")
    private String areacode;
}
