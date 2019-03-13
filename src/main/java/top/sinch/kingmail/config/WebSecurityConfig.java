package top.sinch.kingmail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * web security 配置
 * @author yoking-wi
 * @since 2019年3月13日 15:10:12
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**").permitAll() //api接口直接授权
                .antMatchers("/swagger-ui.html").authenticated() //swagger 需要授权
                .and()
                .formLogin();
    }
}
