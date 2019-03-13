package top.sinch.kingmail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 配置
 *
 * @author yoking-wi
 * @since 2019年3月13日 15:19:48
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.sinch.kingmail.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("国王邮件 API接口文档")
                .description("King Mail API接口文档")
                .contact(new Contact("yoking-wi","https://www.jianshu.com/u/64e4e9db42c9","yoking.wi@foxmail.com"))
                .version("1.0")
                .build();
    }
}
