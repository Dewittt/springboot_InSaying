package dewittt.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class templateEngineConfig {

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect();
    }
}
