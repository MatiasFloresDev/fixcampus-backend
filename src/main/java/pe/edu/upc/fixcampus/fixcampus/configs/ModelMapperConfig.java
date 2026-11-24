package pe.edu.upc.fixcampus.fixcampus.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Con la linea de abajo, ModelMapper ignorará las propiedades cuyo
        // origen sea ambiguo, como idReporte, en lugar de lanzar una excepción.
        mapper.getConfiguration().setAmbiguityIgnored(true);
        return mapper;
    }
}
