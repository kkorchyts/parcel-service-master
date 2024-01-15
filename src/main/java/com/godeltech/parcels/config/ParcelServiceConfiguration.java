package com.godeltech.parcels.config;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import validation.ProtoValidationService;

@Configuration
public class ParcelServiceConfiguration {
  @Bean
  public FakeValuesService fakeValuesService() {
    return new FakeValuesService(Locale.getDefault(), new RandomService());
  }

  @Bean
  public ProtoValidationService validationService() {
    return new ProtoValidationService();
  }
}
