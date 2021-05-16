package com.epam.esm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:application-test.properties")
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//implements ApplicationListener<ApplicationReadyEvent>
//	@Qualifier("jacksonObjectMapper")
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Override
//	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//	}
}
