package com.matchandtrade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Add extra resource handler to expose {@code /files/**} to the directory defined in the {@code file.storage.root.folder} property.
 * 
 * @author rafael.santos.bra@gmail.com
 * @see http://www.baeldung.com/spring-mvc-static-resources
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	private final Logger LOGGER = LoggerFactory.getLogger(MvcConfiguration.class);
	public static final String FILES_URL_PATTERN = "/matchandtrade-web-api/files/**";
	
	@Autowired
	private Environment environment;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String fileStorageRootFolderProperty = "file:" + environment.getProperty(MatchAndTradePropertyKeys.FILE_STORAGE_ROOT_FOLDER.toString());
		LOGGER.info("Exposing static files with the patter [{}] from [{}].", FILES_URL_PATTERN, fileStorageRootFolderProperty);
		registry.addResourceHandler(FILES_URL_PATTERN).addResourceLocations(fileStorageRootFolderProperty);
	}

}