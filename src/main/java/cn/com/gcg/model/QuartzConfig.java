package cn.com.gcg.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Jack on 2018-6-16.
 */
@Entity
public class QuartzConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Column
    @Getter
    @Setter
    private String cron;
}
