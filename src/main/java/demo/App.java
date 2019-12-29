package demo;

import demo.filter.FilterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;

//旧版
/*@EnableAutoConfiguration
@SpringBootApplication*/

//新版
@Controller
@SpringBootApplication
@ServletComponentScan(basePackageClasses = {
        FilterConfiguration.class
})

public class App implements EmbeddedServletContainerCustomizer {

    //修改端口号
    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {

        configurableEmbeddedServletContainer.setPort(7127);
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


}
