package org.sample.applications;

import com.konakart.wsapp.Customer;
import com.konakart.wsapp.CustomerRegistration;
import com.konakart.app.KKException;
import com.konakart.ws.KKWSEngIf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.GregorianCalendar;

@Component
public class KonakartGebruiker {

    private Log log = LogFactory.getLog(KonakartGebruiker.class);

    @Autowired
    KKWSEngIf konakart;

    public KonakartGebruiker() {
    }

    public void registreerGebruiker(){
        System.out.println("Registreer gebruiker");

        CustomerRegistration stefan = new CustomerRegistration();
        stefan.setFirstName("Stefan");
        stefan.setLastName("van der Grift");
        stefan.setEmailAddr("grifts@kadaster.nl");
        stefan.setGender("m");
        stefan.setPassword("stefan");
        stefan.setBirthDate(new GregorianCalendar(1992, 10, 13));
        stefan.setTelephoneNumber("not provided");
        stefan.setStreetAddress("Laan van Westenenk");
        stefan.setPostcode("7334DP");
        stefan.setCity("Apeldoorn");
        stefan.setCountryId(150);
        stefan.setZoneId(149);

        try {
            int customerId = konakart.registerCustomer(stefan);
        } catch (RemoteException e) {
            log.error("Customer bestaat al", e);
        }

        try {
            Customer customer = konakart.getDefaultCustomer();
            customer.getBasketItems();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}