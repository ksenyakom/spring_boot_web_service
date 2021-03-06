package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages =  {
        "com.epam.esm.dao",
        "com.epam.esm.controller",
        "com.epam.esm.service",
        "com.epam.esm.model",
        "com.epam.esm.facade",
        "com.epam.esm.validator",
})

public class TestConfig {

}
