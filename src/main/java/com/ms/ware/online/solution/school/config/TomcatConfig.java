package com.ms.ware.online.solution.school.config;

import org.apache.catalina.Context;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;


import javax.servlet.MultipartConfigElement;

@Configuration
public class TomcatConfig {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addContextCustomizers((Context context) -> {
            if (context.getResources() == null) {
                StandardRoot resources = new StandardRoot(context);
                resources.setCacheMaxSize(40 * 1024 * 1024); // 40 MB
                context.setResources(resources);
            } else {
                context.getResources().setCacheMaxSize(40 * 1024 * 1024);
            }
        });
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.createMultipartConfig();
        factory.setMaxFileSize(DataSize.ofGigabytes(1L));
        factory.setMaxRequestSize(DataSize.ofGigabytes(1L));
        return factory.createMultipartConfig();
    }
}
