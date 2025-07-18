package com.learn.ems.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${com.learn.cloudinary.name}")
    String CLOUD_NAME;

    @Value("${com.learn.cloudinary.api.key}")
    String CLOUD_API_KEY;

    @Value("${com.learn.cloudinary.api.secret}")
    String CLOUD_API_SECRET;

    @Bean
    public Cloudinary cloudinaryConfiguration() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", CLOUD_API_KEY);
        config.put("api_secret", CLOUD_API_SECRET);
        return new Cloudinary(config);
    }
}
