package com.ithe.tushu_guanli_xitong.config;

import com.ithe.tushu_guanli_xitong.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /*
    * 静态资源映射*/
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射!");
        registry.addResourceHandler("/yonghu/**").addResourceLocations("classpath:/yonghu/");
        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/dist/");
        registry.addResourceHandler("/librarystatic/**").addResourceLocations("classpath:/librarystatic/");
        registry.addResourceHandler("/library-management-system-master/**").addResourceLocations("classpath:/library-management-system-master/");
    }
/*
拓展mvc框架的消息转换器
* */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        System.out.println("扩展消息转换器...");
        log.info("扩展消息转换器...");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
