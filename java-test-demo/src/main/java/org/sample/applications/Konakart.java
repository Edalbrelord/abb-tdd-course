package org.sample.applications;

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
    KonakartProducten konakartProducten;

    @Autowired
    KonakartGebruiker konakartGebruiker;

    public void doRun() {
        System.out.println("Konakart Application");

        konakartProducten.zoekProducten();

        CustomerRegistration stefan = konakartGebruiker.getCustomerRegistrationStefan();
        konakartGebruiker.registreerGebruiker(stefan);

        orderProductAsAnonymousUer();
    }

    private void orderProductAsAnonymousUer() {
        try {
//            Login (default) user
            CustomerRegistration stefan = konakartGebruiker.getCustomerRegistrationStefan();
            String sessionId = konakart.login(stefan.getEmailAddr(), stefan.getPassword());
            Customer customer = konakart.getCustomer(sessionId);

            Basket[] basketItemArray = new Basket[0];

            Product product = konakartProducten.getAvailableProduct();

            List<Basket> basketProducts = new ArrayList<Basket>();

//            Add product to basket (Product + quantity + additional info)
            Basket basketProduct = new Basket();
            basketProduct.setQuantity(1);
            basketProduct.setProductId(product.getId());

//            Push basket to server
            int basketId = konakart.addToBasket(sessionId, customer.getId(), basketProduct);
            log.info("Created basket with ID: " + basketId);

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
}