package pe.edu.upc.fixcampus.fixcampus.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();


        mapper.getConfiguration().setAmbiguityIgnored(true);
        return mapper;
    }
}
