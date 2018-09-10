package cn.com.gcg.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jack on 2018-8-1.
 */
@ToString
@Data
@Table(name = "monitor_phone_task")
@Entity
public class PhoneTaskConfig {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //电话号码
    @Column(name = "phone_number")
    private String phoneNumber;

    //音频文件
    @Column(name = "voice_file")
    private String voiceFile;

    //数据源 0 本系统 1 其他系统
    @Column(name = "data_source")
    private Integer dataSource;

    //新增时间
    @Column(name = "createtime")
    private Date createTime;

    //地区编码  非本系统为999999
    @Column(name = "areacode")
    private String areaCode;




}
