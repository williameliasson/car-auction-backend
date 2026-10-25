package com.williameliasson.car_auction_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.williameliasson.car_auction_backend.models.Order;

import com.stripe.param.checkout.SessionCreateParams;

@Service 
public class StripeService {

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String webhookSecret;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    StripeService(){
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(Order order) throws Exception{
        // make sure the is not already paid
        if (order.isPaid()){
            throw new IllegalArgumentException("Order is already paid for");
        }

        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setClientReferenceId(order.getId().toString())
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("sek")
                            .setUnitAmount(order.getAmountSek() * 100L) // Expects integer Long value in öre
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Order #" + order.getId())
                                    .setDescription("Deposit to your account")
                                    .build()
                            ).build()
                    ).build()
            ).build();

        // create a session object and tell Stripe about it
        Session session = Session.create(params);
        return session.getUrl();
    }   

    public void handleWebhook(String payload, String sigHeader){

    }
}
