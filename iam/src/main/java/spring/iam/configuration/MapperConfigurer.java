package spring.iam.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigurer {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
