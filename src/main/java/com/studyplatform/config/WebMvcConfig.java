package com.studyplatform.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * Configuration for setting up a separate servlet dispatcher for the root URL
 * This allows the application to handle requests to both the root URL and the /api URL
 */
@Configuration
public class WebMvcConfig {

    /**
     * Register a servlet dispatcher for the root URL
     * @return ServletRegistrationBean for the root URL
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> rootDispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        // Create a separate application context for the root URL
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(StaticResourceConfig.class);
        dispatcherServlet.setApplicationContext(applicationContext);

        // Register the servlet for the root URL
        ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = 
            new ServletRegistrationBean<>(dispatcherServlet, "/");
        servletRegistrationBean.setName("rootDispatcher");
        servletRegistrationBean.setLoadOnStartup(1);

        return servletRegistrationBean;
    }

    /**
     * Customize the web server factory to ensure proper handling of multiple servlets
     * @return WebServerFactoryCustomizer
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setRegisterDefaultServlet(false);
    }
}
