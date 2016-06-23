package org.sample.applications;

import com.konakart.app.DataDescConstants;
import com.konakart.wsapp.DataDescriptor;
import com.konakart.app.KKException;
import com.konakart.wsapp.Product;
import com.konakart.wsapp.ProductSearch;
import com.konakart.appif.*;
import com.konakart.ws.KKWSEngIf;
import com.konakart.wsapp.Products;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

@Component
public class KonakartProducten {

    private Log log = LogFactory.getLog(KonakartProducten.class);

    @Autowired
    KKWSEngIf konakart;

    public KonakartProducten() {
    }

    void zoekProducten() {
        getAmountOfProducts(10);

        getProductWithName("Warner");

        getProductWithDescription("Spanish");
    }

    Products getProductsWithCategory(int categoryId){
        Products products = null;

        DataDescriptor dataDesc = new DataDescriptor();
        dataDesc.setOrderBy(DataDescConstants.ORDER_BY_RATING);
        dataDesc.setLimit(100);
        dataDesc.setOffset(0);

        ProductSearch productSearch = new ProductSearch();
        productSearch.setManufacturerId(-100);              // -100 == Search all
        productSearch.setCategoryId(3);                  // -100 == Search all
        productSearch.setSearchInSubCats(true);
//        productSearch.setSearchAllStores(false);
//        productSearch.setProductType(-1);

        try {
            Products allProducts = konakart.getAllProducts(null, dataDesc, -1);
            products = konakart.searchForProducts(null, dataDesc, productSearch, -1);
            System.out.println("Found " + products.getTotalNumProducts() + " products:");

            for (Product product : products.getProductArray()) {
                System.out.println(product.getManufacturerName() + " - " + product.getName() + ": " + product.getDescription());
            }
        } catch (RemoteException e) {
            log.error(e);
        }

        return products;
    }

    private void getProductWithDescription(String name) {
        System.out.println("Get products with description: " + name);

        DataDescriptor dataDesc = new DataDescriptor();
        dataDesc.setLimit(100);

        ProductSearch productSearch = new ProductSearch();
        productSearch.setSearchText(name);
        productSearch.setFillDescription(true);
        productSearch.setWhereToSearch(com.konakart.app.ProductSearch.SEARCH_IN_PRODUCT_DESCRIPTION);

        try {
            Products products = konakart.searchForProducts(null, dataDesc, productSearch, -1);
            System.out.println("Found " + products.getTotalNumProducts() + " products:");

            for (Product product : products.getProductArray()) {
                System.out.println(product.getManufacturerName() + " - " + product.getName() + ": " + product.getDescription());
            }

        } catch (RemoteException e) {
            log.error(e);
        }
    }

    private void getProductWithName(String name) {
        System.out.println("Get products with name: " + name);

        DataDescriptor dataDesc = new DataDescriptor();
        dataDesc.setLimit(100);

        ProductSearch productSearch = new ProductSearch();
        productSearch.setSearchText(name);
        productSearch.setFillDescription(true);

        try {
            Products products = konakart.searchForProducts(null, dataDesc, productSearch, -1);
            System.out.println("Found " + products.getTotalNumProducts() + " products:");

            for (Product product : products.getProductArray()) {
                System.out.println(product.getManufacturerName() + " - " + product.getName() + ": " + product.getDescription());
            }

        } catch (RemoteException e) {
            log.error(e);
        }
    }

    private void getAmountOfProducts(int amount) {
        System.out.println("Get " + amount + " products");
        DataDescriptor dataDesc = new DataDescriptor();
        dataDesc.setLimit(amount);

        ProductSearch prodSearch = new ProductSearch();
        try {
            Products products = konakart.searchForProducts(null, dataDesc, prodSearch, -1);
            System.out.println("Found " + products.getTotalNumProducts() + " products:");

            for (Product product : products.getProductArray()) {
                System.out.println(product.getManufacturerName() + " - " + product.getName() + ": " + product.getDescription());
            }

        } catch (RemoteException e) {
            log.error(e);
        }
    }
}