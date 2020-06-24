package cn.sanii.vx_server.config;

import cn.sanii.earth.pipeline.IPipeline;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下 Linux请自行设置好路径再部署
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/image/**").addResourceLocations("file:"+ IPipeline.FILE_PATH);
        } else {
            registry.addResourceHandler("/image/**").addResourceLocations("/home/pi/vx_server/");
        }
    }

}
