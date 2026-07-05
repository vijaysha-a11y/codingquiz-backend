package com.config;
<<<<<<< HEAD

=======
>>>>>>> 3e55ba82dcadcaf7bd728494d80568cac9a2fc6d
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
<<<<<<< HEAD
public class webConfig implements WebMvcConfigurer {
=======
public class webConfig implements WebMvcConfigurer{
>>>>>>> 3e55ba82dcadcaf7bd728494d80568cac9a2fc6d
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://codingquiz-frontend.onrender.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
<<<<<<< HEAD
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
=======
                .allowCredentials(true);
    } 
}

>>>>>>> 3e55ba82dcadcaf7bd728494d80568cac9a2fc6d
