package cn.com.gcg.config;

import cn.com.gcg.telbox.InitializeBox;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Jack on 2018-6-13.
 */
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        //开启电话盒子
        InitializeBox.initialize();
    }


}
