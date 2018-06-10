package cn.com.gcg;

import cn.com.gcg.telbox.InitializeBox;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by Jack on 2018/6/3.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);

        //开启电话盒子1
        InitializeBox.initialize();
    }
}
