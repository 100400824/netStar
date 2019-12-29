package demo.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.util.logging.Logger;

@Configuration
public class FilterConfiguration implements ServletContextInitializer {

    private static Logger logger = Logger.getLogger(FilterConfiguration.class.getName());
    @Bean
    public FilterRegistrationBean crossFilterConfiguration(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(crossFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("crossOriginFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean(name = "crossOriginFilter")
    public Filter crossFilter(){

        return new CrossFilter();
    }


    @Override
    public void onStartup(ServletContext servletContext)  {
        logger.info("Web application fully configured");
    }
}
