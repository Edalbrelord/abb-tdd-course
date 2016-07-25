package org.sample.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Stefan van der Grift
 * @since 20-6-2016
 */
@Configuration
@ComponentScan(basePackages = {"org.sample"})
public class ApplicationConfig {
}
