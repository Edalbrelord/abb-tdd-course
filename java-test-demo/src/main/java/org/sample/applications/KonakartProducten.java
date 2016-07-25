package org.sample.applications;

import com.konakart.app.DataDescConstants;
import com.konakart.ws.KKWSEngIf;
import com.konakart.wsapp.*;
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

        getProductWithName("Bosch");

        getProductWithDescription("Soft-Touch");
    }

    public Products getProductsWithCategory(int categoryId) {
        Products products = null;

        DataDescriptor dataDescriptor = getDefaultDataDescriptor();
        dataDescriptor.setOrderBy(DataDescConstants.ORDER_BY_RATING);
        dataDescriptor.setLimit(100);

        ProductSearch productSearch = getDefaultProductSearch();
        productSearch.setCategoryId(categoryId);

        try {
            products = konakart.searchForProducts(null, dataDescriptor, productSearch, -1);
        } catch (RemoteException e) {
            log.error(e);
        }

        return products;
    }

    public Product getAvailableProduct(){
        Product product = null;

        DataDescriptor dataDescriptor = getDefaultDataDescriptor();
        dataDescriptor.setLimit(1);
        dataDescriptor.setShowInvisible(true);

        ProductSearch productSearch = getDefaultProductSearch();
        productSearch.setSearchText("Rotak 40 Ergoflex");

        try {
            Products products = konakart.searchForProducts(null, dataDescriptor, productSearch, -1);
            product = products.getProductArray()[0];
        } catch (RemoteException e) {
            log.error(e);
        }

        return product;
    }

    private void getProductWithDescription(String description) {
        System.out.println("Get products with description: " + description);

        DataDescriptor dataDescriptor = getDefaultDataDescriptor();
        dataDescriptor.setShowInvisible(true);

        ProductSearch productSearch = getDefaultProductSearch();
        productSearch.setSearchText(description);
        productSearch.setFillDescription(true);
        productSearch.setWhereToSearch(com.konakart.app.ProductSearch.SEARCH_IN_PRODUCT_DESCRIPTION);

//        TODO: Not returning products...
        try {
            Products products = konakart.searchForProducts(null, dataDescriptor, productSearch, -1);
            printProducts(products);

        } catch (RemoteException e) {
            log.error(e);
        }
    }

    private void getProductWithName(String name) {
        System.out.println("Get products with name: " + name);

        DataDescriptor dataDescriptor = getDefaultDataDescriptor();

        ProductSearch productSearch = getDefaultProductSearch();
        productSearch.setSearchText(name);
        productSearch.setFillDescription(true);

        try {
            Products products = konakart.searchForProducts(null, dataDescriptor, productSearch, -1);
            printProducts(products);

        } catch (RemoteException e) {
            log.error(e);
        }
    }

    private void getAmountOfProducts(int amount) {
        System.out.println("Get " + amount + " products");

        DataDescriptor dataDescriptor = getDefaultDataDescriptor();
        dataDescriptor.setLimit(amount);

        ProductSearch prodSearch = getDefaultProductSearch();

        try {
            Products products = konakart.searchForProducts(null, dataDescriptor, prodSearch, -1);
            printProducts(products);

        } catch (RemoteException e) {
            log.error(e);
        }
    }

    private DataDescriptor getDefaultDataDescriptor() {
        DataDescriptor dataDesc = new DataDescriptor();
        dataDesc.setOffset(0);
        dataDesc.setLimit(100);

        return dataDesc;
    }

    private ProductSearch getDefaultProductSearch() {
        ProductSearch productSearch = new ProductSearch();
        productSearch.setManufacturerId(-100);              // -100 == Search all
        productSearch.setCategoryId(-100);                  // -100 == Search all
        productSearch.setSearchInSubCats(true);

        return productSearch;
    }

    private void printProducts(Products products) {
        log.info("Found " + products.getProductArray().length + " products:");

        for (Product product : products.getProductArray()) {
            log.debug(product.getManufacturerName() + " - " + product.getName() + ": " + product.getDescription());
        }
    }
}