package top.sinch.kingmail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan("top.sinch.kingmail")
@MapperScan("top.sinch.kingmail.dao")
public class KingMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingMailApplication.class, args);
    }

}
