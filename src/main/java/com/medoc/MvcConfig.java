package com.medoc;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("user-photos", registry);
		exposeDirectory("../site-logo", registry);
		exposeDirectory("../ordo-images", registry);
	}
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("../", "") + "/**";
		String resourceLocation = "file:" + absolutePath + "/";
		
		System.out.println("Registering resource handler:");
		System.out.println("  Logical path: " + logicalPath);
		System.out.println("  Physical location: " + resourceLocation);
				
		registry.addResourceHandler(logicalPath)
			.addResourceLocations(resourceLocation);		
	}

}
