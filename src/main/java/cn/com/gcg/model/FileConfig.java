package cn.com.gcg.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jack on 2018/6/10.
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@Data
@Entity
@Table(name = "monitor_file")
public class FileConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="file_path")
    private String filePath;

    @Column(name="file_name")
    private String fileName;

    @Column(name="monitor_time")
    private String monitorTime;

    @Column(name="enabled")
    private Integer enabled;

    @Column(name="areacode")
    private String areacode;

    @Column(name="status")
    private Integer status;

    @Column(name="last_onlinetime")
    private String lastOnlinetime;

}
