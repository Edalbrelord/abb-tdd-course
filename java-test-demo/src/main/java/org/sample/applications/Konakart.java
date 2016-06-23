package org.sample.applications;

import com.konakart.appif.ProductsIf;
import com.konakart.wsapp.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Konakart API POC
 *
 * @author Stefan van der Grift
 * @since 22-06-2016
 */
@Component
public class Konakart {

    @Autowired
    KonakartProducten konakartProducten;

    @Autowired
    KonakartGebruiker konakartGebruiker;

    private Products products;

    public void doRun() {
        System.out.println("Konakart Application");

        konakartProducten.zoekProducten();

        products = konakartProducten.getProductsWithCategory(3);

        konakartGebruiker.registreerGebruiker();
    }

}