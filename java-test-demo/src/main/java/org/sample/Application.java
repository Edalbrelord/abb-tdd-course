package org.sample;

import org.apache.log4j.PropertyConfigurator;
import org.sample.applications.Konakart;
import org.sample.applications.Sort;
import org.sample.config.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
//            System.out.println("provide an argument to start an application");
            xmlLib();
        }

    }

    private static void xmlLib() {
        // build the object
        Dependencies dependencies = new Dependencies();
        List<Dependency> dependencyList = new ArrayList<>();

        File konakartLibs = new File("D:\\Tools\\Konakart\\webapps\\konakart\\WEB-INF\\lib");

        dependencies.setDependencyList(dependencyList);
        for(File file : konakartLibs.getAbsoluteFile().listFiles()){
            Dependency dependency = new Dependency();
            dependency.setArtifactId(file.getName());
            dependency.setGroupId("com.konakart");
            dependency.setScope("system");
            dependency.setSystemPath(file.getAbsolutePath().replace("D:\\Tools\\Konakart\\", "${project.basedir}\\..\\..\\"));
            dependency.setVersion("1.0");

            dependencyList.add(dependency);
        }


        File file = new File("D:\\file.xml");
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Dependencies.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(dependencies, file);
//            jaxbMarshaller.marshal(dependencies, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
