package com.williameliasson.car_auction_backend.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration 
public class OpenAIRestTemplate {
    @Value("${OPENAI_KEY}")
    private String apiKey;

    @Bean 
    @Qualifier("openAIRestTemplate")
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((req, body, exe) -> {
            req.getHeaders().add("Authorization", "Bearer " + apiKey);
            req.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return exe.execute(req, body);
        });

        return restTemplate;
    }
}
