package cn.com.gcg.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Data
@Entity
@Table(name = "monitor_ip")
public class IpConfig {

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

}
