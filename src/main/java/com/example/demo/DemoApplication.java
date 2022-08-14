package com.example.demo;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);

  }

  @Bean
  public ModelMapper modelMapper() {
    final ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }
}
