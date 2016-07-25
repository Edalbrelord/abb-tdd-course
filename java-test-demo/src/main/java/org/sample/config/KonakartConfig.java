package org.sample.config;

import com.konakart.app.KKException;
import com.konakart.appif.KKEngIf;
import com.konakart.bl.KKEngineUtils;
import com.konakart.util.KKConstants;
import com.konakart.ws.KKWSEngIf;
import com.konakart.ws.KKWSEngIfServiceLocator;
import com.konakart.wsapp.EngineConfig;
import org.apache.axis.configuration.EngineConfigurationFactoryServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.rpc.ServiceException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by SGRIFT on 23-6-2016.
 */
@Configuration
public class KonakartConfig {

    @Bean
    KKWSEngIf getKonakartEngine() throws KKException, ServiceException, RemoteException, MalformedURLException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {

        KKWSEngIfServiceLocator kkwsEngIfServiceLocator = new KKWSEngIfServiceLocator();

//        KKWSEngIf kkWebServiceEng = kkwsEngIfServiceLocator.getKKWebServiceEng();

//        EngineConfig engConf = kkWebServiceEng.getEngConf();
//        engConf.setAppPropertiesFileName(KKConstants.KONAKART_APP_PROPERTIES_FILE);
//        engConf.setPropertiesFileName(KKConstants.KONAKART_PROPERTIES_FILE);
//        engConf.setStoreId(KKConstants.KONAKART_DEFAULT_STORE_ID);

        return kkwsEngIfServiceLocator.getKKWebServiceEng(new URL("http://localhost:8780/konakart/services/KKWebServiceEng"));
    }

    @Bean
    KKEngIf getKonakartRMIEngine() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        com.konakart.app.EngineConfig engConf = new com.konakart.app.EngineConfig();

        engConf.setMode(0);
        engConf.setStoreId("store1");
        engConf.setCustomersShared(true);
        engConf.setProductsShared(true);
        engConf.setCategoriesShared(true);

        /*
         * Instantiate a KonaKart Engine. Different engines can be instantiated by passing
         * KKWSEngName or KKRMIEngName or KKJSONEngName for the SOAP, RMI or JSON engines
         */
        KKEngIf kkEngByName = new KKEngineUtils().getKKEngByName("com.konakart.rmi.KKRMIEng", engConf);
        return kkEngByName;
    }
}
