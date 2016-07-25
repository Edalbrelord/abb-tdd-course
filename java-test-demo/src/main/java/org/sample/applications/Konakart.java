package org.sample.applications;

import com.konakart.app.KKException;
import com.konakart.appif.*;
import com.konakart.ws.KKWSEngIf;
import com.konakart.wsapp.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Konakart API POC
 *
 * @author Stefan van der Grift
 * @since 22-06-2016
 */
@Component
public class Konakart {

    private Log log = LogFactory.getLog(Konakart.class);

    @Autowired
    KKWSEngIf konakart;

    @Autowired
    KKEngIf konakartRmi;

    @Autowired
    KonakartProducten konakartProducten;

    @Autowired
    KonakartGebruiker konakartGebruiker;

    public void doRun() {
        System.out.println("Konakart Application");

        konakartProducten.zoekProducten();

        CustomerRegistration stefan = konakartGebruiker.getCustomerRegistrationStefan();
        konakartGebruiker.registreerGebruiker(stefan);

        orderProductAsStefan();
//        orderProductAsAnonymousUser();
//        rmiDemo();
    }

    private void rmiDemo() {
        DataDescriptorIf dataDesc = new com.konakart.app.DataDescriptor();
        dataDesc.setOffset(0);
        dataDesc.setLimit(100);

        ProductSearchIf productSearch = new com.konakart.app.ProductSearch();
        productSearch.setManufacturerId(-100);              // -100 == Search all
        productSearch.setCategoryId(-100);                  // -100 == Search all
        productSearch.setSearchInSubCats(true);

        ProductsIf productsIf = null;
        try {
            productsIf = konakartRmi.searchForProducts(null, dataDesc, productSearch, -1);

            ProductIf product = konakartRmi.getProduct(null, 1, -1);

            BasketIf basketItem = new com.konakart.app.Basket();
            basketItem.setProductId(product.getId());
            basketItem.setQuantity(1);
            basketItem.setCustom1("test");

            konakartRmi.addToBasket(null, -1, basketItem);

            BasketIf[] basketItemsPerCustomer = konakartRmi.getBasketItemsPerCustomer(null, -1, -1);

            CreateOrderOptionsIf createOrderOptions = new com.konakart.app.CreateOrderOptions();
            createOrderOptions.setUseDefaultCustomer(true);

            OrderIf orderWithOptions = konakartRmi.createOrderWithOptions(null, basketItemsPerCustomer, createOrderOptions, -1);

//            Select Cash on Demand
            orderWithOptions.setPaymentMethod("cod");

            OrderStatusHistoryIf orderStatus = new com.konakart.app.OrderStatusHistory();
            orderStatus.setOrderStatusId(1);
            orderStatus.setCustomerNotified(false);

            OrderStatusHistoryIf[] orderStatusHistoryList = {orderStatus};
            orderWithOptions.setStatusTrail(orderStatusHistoryList);

            OrderIf orderTotals = konakartRmi.getOrderTotals(orderWithOptions, -1);

            String sessionId = konakartRmi.login("grifts@kadaster.nl", "stefan");

            int orderId = konakartRmi.saveOrder(sessionId, orderTotals, -1);

            OrdersIf ordersPerCustomer = konakartRmi.getOrdersPerCustomer(dataDesc, sessionId, -1);
            OrderIf order = konakartRmi.getOrder(sessionId, orderId, -1);

            log.info(productsIf);
        } catch (KKException e) {
           log.error("RMI:", e);
        }
    }

    private void orderProductAsStefan() {
        try {

//            Login user
            CustomerRegistration stefan = konakartGebruiker.getCustomerRegistrationStefan();
            String sessionId = konakart.login(stefan.getEmailAddr(), stefan.getPassword());
            Customer customer = konakart.getCustomer(sessionId);

            Product prod = konakartProducten.getAvailableProduct();

//            Retrieve full product with options
            Product product = konakart.getProduct(sessionId, prod.getId(), -1);

//            Add product to basket (Product + quantity + additional info)
            Basket basketProduct = new Basket();
            basketProduct.setProduct(product);
            basketProduct.setQuantity(1);
            basketProduct.setProductId(product.getId());

            // Create an Option and add a couple of the available product options
            List<Option> options = new ArrayList<>();
            Option option1 = new Option();

            int optionId = 0;
            int optionValueId = 0;
            for( Option option : product.getOpts()){
                if("flightcase".equals(option.getCode()) && "kadastrale_aanduiding".equals(option.getValueCode())){
                    optionId = option.getId();
                    optionValueId = option.getValueId();
                }
            }

            option1.setId(optionId);
            option1.setValueId(optionValueId);
            option1.setCustomerText("NLGOES123");

            options.add(option1);

            basketProduct.setOpts(options.toArray(new Option[options.size()]));

//            Push basket to server
            int basketId = konakart.addToBasket(sessionId, 0, basketProduct);
            log.info("Created basket with ID: " + basketId);

            // Retrieve basket (for overview), also needed as Konakart adds additional values
            Basket[] basketItemsPerCustomer = konakart.getBasketItemsPerCustomer(sessionId, customer.getId(), -1);

            Order order = konakart.createOrder(sessionId, basketItemsPerCustomer, -1);
            if(order != null){
                order = konakart.getOrderTotals(order, -1);
                log.info("Created order with total: " + order.getOrderTotals()[1].getValue());

//                Payment details
                PaymentDetails cashOnDelivery = konakart.getPaymentDetailsPerOrder(sessionId, "cod", order, "http://localhost:8780", -1);
                order.setPaymentDetails(cashOnDelivery);

                OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
                orderStatusHistory.setOrderId(order.getId());
                orderStatusHistory.setOrderStatusId(2);

                OrderStatusHistory[] orderStatusTrail = new OrderStatusHistory[] {orderStatusHistory};
                order.setStatusTrail(orderStatusTrail);

                int orderId = konakart.saveOrder(sessionId, order, -1);
                log.info("Saved order with ID: " + orderId);

//                Ship the order
                // Update the inventory
                konakart.updateInventory(sessionId, orderId);
//                Reset the basket for a next purchase
                konakart.removeBasketItemsPerCustomer(sessionId, customer.getId());

            }
            else{
                log.error("Order could not be created");
            }

        } catch (RemoteException e) {
            log.error("Er trad een fout op tijdens het toevoegen aan de basket", e);
        }
    }

    private void orderProductAsAnonymousUser(){
        Customer customer = null;
        Product konakartProduct = null;
        String sessionId = null;

        try {
            customer = konakart.getDefaultCustomer();

        } catch (RemoteException e) {
            log.error("Could not get default user", e);
        }

        Product availableProduct = konakartProducten.getAvailableProduct();
        try {
            konakartProduct = konakart.getProduct(null, availableProduct.getId(), -1);
        } catch (RemoteException e) {
            log.error("Could not get product", e);
        }

        if(konakartProduct != null && customer != null){
            Basket basketProduct = new Basket();
            basketProduct.setProductId(konakartProduct.getId());
            basketProduct.setQuantity(1);

            try {
                int basketId = konakart.addToBasket(null, -1, basketProduct);
                log.info("Created basket with ID: " + basketId);

                // Retrieve basket (for overview), also needed as Konakart adds additional values
                Basket[] basketItemsPerCustomer = konakart.getBasketItemsPerCustomer(null, -1, -1);

                log.info("Retrieved basket with ID: " + basketId + " from Konakart");

                CreateOrderOptions orderOptions = new CreateOrderOptions();
                orderOptions.setUseDefaultCustomer(true);

                Order order = konakart.createOrderWithOptions(null, basketItemsPerCustomer, orderOptions, -1);
                if(order != null){
                    order = konakart.getOrderTotals(order, -1);
                    log.info("Created order with total: " + order.getOrderTotals()[1].getValue());

//                Payment details
                    PaymentDetails cashOnDelivery = konakart.getPaymentDetailsPerOrder(null, "cod", order, "http://localhost:8780", -1);
                    order.setPaymentDetails(cashOnDelivery);

                    OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
                    orderStatusHistory.setOrderId(order.getId());
                    orderStatusHistory.setOrderStatusId(2);

                    OrderStatusHistory[] orderStatusTrail = new OrderStatusHistory[] {orderStatusHistory};
                    order.setStatusTrail(orderStatusTrail);

//                    Register user

//                    Login user

                    sessionId = "new sessionId";

                    int orderId = konakart.saveOrder(sessionId, order, -1);
                    log.info("Saved order with ID: " + orderId);

//                Ship the order
                    // Update the inventory
                    konakart.updateInventory(sessionId, orderId);
//                Reset the basket for a next purchase
                    konakart.removeBasketItemsPerCustomer(sessionId, customer.getId());

                }
                else{
                    log.error("Order could not be created");
                }
            } catch (RemoteException e) {
                log.error(e);
            }

        }
    }
}