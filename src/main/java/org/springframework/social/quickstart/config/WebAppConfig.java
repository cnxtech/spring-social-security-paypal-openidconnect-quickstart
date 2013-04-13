package org.springframework.social.quickstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.quickstart.user.UserInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.inject.Inject;


@Configuration
@EnableWebMvc
public class WebAppConfig extends WebMvcConfigurerAdapter {

     @Inject
     private UsersConnectionRepository usersConnectionRepository;

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass( JstlView.class );
        viewResolver.setPrefix( "/WEB-INF/views/" );
        viewResolver.setSuffix( ".jsp" );
        return viewResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(usersConnectionRepository));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/signin");
        registry.addViewController("/signout");
    }
}
