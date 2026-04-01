package com.guru.ecommerce.Security;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.guru.ecommerce")
public class Configs {

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
