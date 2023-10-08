#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure.general.config;

import com.eula.component.result.ResponseResultInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResponseResultInterceptor());
    }

    /**
     * 注册 CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 项目中的所有接口都支持跨域
                .allowedOriginPatterns("*") // 所有地址都可以访问，也可以配置具体地址
                .allowCredentials(true)
                .allowedMethods("*") // "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
                .maxAge(3600); // 跨域允许时间
    }

    /**
     * 注册类型转化器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        //序列化将BigDecimal转String类型
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);

        // 配置支持的类型
        List<MediaType> mediaTypes = Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.TEXT_XML,
                MediaType.APPLICATION_OCTET_STREAM,
                new MediaType("application", "*+json"));
        jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        converters.add(jackson2HttpMessageConverter);
    }

}
