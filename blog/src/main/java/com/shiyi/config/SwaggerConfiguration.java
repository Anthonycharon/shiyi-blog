package com.shiyi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.shiyi.utils.JacksonAnnotationIntrospectorEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * api接口文档
 *
 * @author blue
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/.*";
//    public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ObjectMapper setObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospectorEx());
        return objectMapper;
    }

    /**
     * Swagger Springfox configuration.
     *
     * @param properties the properties of the application
     * @return the Swagger Springfox configuration
     */
    @Bean
    public Docket swaggerSpringfoxDocket(MyProperties properties) {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("Authorization").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        LOG.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(
                properties.getSwagger().getContactName(),
                properties.getSwagger().getContactUrl(),
                properties.getSwagger().getContactEmail());

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(properties.getSwagger().getTitle())
                .description(properties.getSwagger().getDescription())
                .version(properties.getSwagger().getVersion())
                .termsOfServiceUrl(properties.getSwagger().getTermsOfServiceUrl())
                .contact(contact)
                .license(properties.getSwagger().getLicense())
                .licenseUrl(properties.getSwagger().getLicenseUrl()).build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build().globalOperationParameters(pars);;

        watch.stop();
        LOG.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
