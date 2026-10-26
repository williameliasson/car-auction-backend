package com.williameliasson.car_auction_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.williameliasson.car_auction_backend.models.Order;

import com.stripe.param.checkout.SessionCreateParams;

@Service 
public class StripeService {

    private final OrderService orderService;

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String webhookSecret;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    StripeService(OrderService orderService){
        this.orderService = orderService;
    }
    
    public String createCheckoutSession(Order order) throws Exception{
        Stripe.apiKey = secretKey;

        // make sure the is not already paid
        if (order.isPaid()){
            throw new IllegalArgumentException("Order is already paid for");
        }

        SessionCreateParams params = SessionCreateParams.builder()
            .setCancelUrl( frontendUrl + "/account?successfulPayment=false")
            .setSuccessUrl(frontendUrl + "/account?successfulPayment=true" )
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

    public void handleWebhook(String payload, String sigHeader) throws Exception{
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        if (event.getType().equals("checkout.session.completed")){
            Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalArgumentException("Could not deserialize event object"));
            
            String orderId = session.getClientReferenceId();
            Order order = orderService.getOrderById(Integer.valueOf(orderId));
            orderService.markAsPaid(order);
        }
    }
}
