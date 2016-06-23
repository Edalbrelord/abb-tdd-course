package org.sample.services;

import com.konakart.app.KKException;
import com.konakart.app.KKWSEng;
import com.konakart.appif.CustomerIf;
import com.konakart.appif.EngineConfigIf;
import com.konakart.wsapp.Customer;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;

/**
 * Created by SGRIFT on 23-6-2016.
 */
public class KKWSCustomEngine extends KKWSEng {

    public KKWSCustomEngine(EngineConfigIf engineConfigIf) throws KKException {
        super(engineConfigIf);
    }
}
