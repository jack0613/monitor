package cn.com.gcg.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
//@ToString
@Data
@Entity
@Table(name = "monitor_ip")
public  class IpConfig implements  Serializable {

    private static final long serialVersionUID = -2806739665446705219L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ip_addr")
    private String ipAddr;

    @Column(name="ip_name")
    private String ipName;

    @Column(name="enabled")
    private Integer enabled;

    @Column(name="areacode")
    private String areacode;

    @Column(name="status")
    private Integer status;

    @Column(name="last_onlinetime")
    private String lastOnlinetime;

}
