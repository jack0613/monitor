package cn.com.gcg.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Data
@Table(name="monitor_user")
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 3456029483581204658L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="Login_name")
    private String loginName;
    @Column(name="Login_pwd")
    private String loginPwd;
    @Column(name="Nick_name")
    private String nickName;
    @Column(name="areacode")
    private String areacode;


}
