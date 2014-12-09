package org.ironrhino.sample.api;

import org.ironrhino.core.servlet.HttpErrorHandler;
import org.ironrhino.rest.ApiConfigBase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.ControllerAdvice;

//only for exclude-filter of root ctx
@ControllerAdvice
@Configuration
@PropertySources({
		@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources/spring/applicationContext.properties"),
		@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources/spring/applicationContext.${STAGE}.properties"),
		@PropertySource(ignoreResourceNotFound = true, value = "file:${app.home}/conf/applicationContext.properties"),
		@PropertySource(ignoreResourceNotFound = true, value = "file:${app.home}/conf/applicationContext.${STAGE}.properties") })
@ComponentScan(excludeFilters = @Filter(value = HttpErrorHandler.class, type = FilterType.ASSIGNABLE_TYPE))
@EnableAspectJAutoProxy
public class ApiConfig extends ApiConfigBase {

}