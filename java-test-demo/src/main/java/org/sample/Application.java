package org.sample;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.sample.applications.Konakart;
import org.sample.applications.Sort;
import org.sample.config.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Generic Application
 *
 * @author Stefan van der Grift
 * @since 20-6-2016
 */
@Configuration
@ComponentScan
public class Application {

    public static void main(String[] args) throws IOException {
        String log4jConfPath = "/log4j.properties";
        Properties properties = new Properties();
        InputStream resourceAsStream = Application.class.getResourceAsStream(log4jConfPath);
        properties.load(resourceAsStream);
        PropertyConfigurator.configure(properties);

        if (args != null && args.length > 0) {

//            Create a Spring Context
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

//            Setup configuration for applications
            applicationContext.register(ApplicationConfig.class);

//            Start spring context
            applicationContext.refresh();
            applicationContext.start();

            switch (args[0]) {
                case "sort":
                    System.out.println("Sorting application");

                    Sort sort = applicationContext.getBean(Sort.class);
                    sort.run();

                    break;

                case "konakart":
                    Konakart konakart = applicationContext.getBean(Konakart.class);
                    konakart.doRun();
                    break;
            }
        } else {
            System.out.println("provide an argument to start an application");
        }
    }
}
