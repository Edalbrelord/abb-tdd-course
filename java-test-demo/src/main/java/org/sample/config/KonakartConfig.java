package org.sample.config;

import com.konakart.app.KKException;
import com.konakart.util.KKConstants;
import com.konakart.ws.KKWSEngIf;
import com.konakart.ws.KKWSEngIfServiceLocator;
import com.konakart.wsapp.EngineConfig;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.EngineConfigurationFactoryDefault;
import org.apache.axis.configuration.EngineConfigurationFactoryServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by SGRIFT on 23-6-2016.
 */
@Configuration
public class KonakartConfig {

    @Bean
    KKWSEngIf getKonakartEngine() throws KKException, ServiceException, RemoteException, MalformedURLException {

        KKWSEngIfServiceLocator kkwsEngIfServiceLocator = new KKWSEngIfServiceLocator();

        KKWSEngIf kkWebServiceEng = kkwsEngIfServiceLocator.getKKWebServiceEng();
        EngineConfig engConf = kkWebServiceEng.getEngConf();
        engConf.setAppPropertiesFileName(KKConstants.KONAKART_APP_PROPERTIES_FILE);
        engConf.setPropertiesFileName(KKConstants.KONAKART_PROPERTIES_FILE);
        engConf.setStoreId(KKConstants.KONAKART_DEFAULT_STORE_ID);

        EngineConfigurationFactoryServlet.newFactory(null);

        return kkwsEngIfServiceLocator.getKKWebServiceEng(new URL("http://localhost:8780/konakart/services/KKWebServiceEng"));
    }


}
