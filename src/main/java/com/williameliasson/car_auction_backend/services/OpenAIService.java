package com.williameliasson.car_auction_backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.williameliasson.car_auction_backend.dtos.ValuationResult;
import com.williameliasson.car_auction_backend.models.Auction;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service 
public class OpenAIService {
    public final RestTemplate restTemplate;
    
    @Value("${OPENAI_URL}")
    String apiUrl;
    
    OpenAIService(@Qualifier("openAIRestTemplate") RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    
    private String getValuationQuestion(Auction auction){
        return "What is the value of a used car of this spec in SEK:" + auction.getMake().getName() + auction.getTitle() + " from " + auction.getModelYear() + " with " + auction.getOdometerKm() + " km on the odometer? Description written by seller: " + auction.getDescription();
    }

    public ValuationResult getValuation(Auction auction){
        // src: https://developers.openai.com/api/docs/guides/structured-outputs?api-mode=responses
        Map<String, Object> schema =
            Map.of(
                "type", "object",
                "properties", Map.of(
                    "valuationMin", Map.of("type", "integer"),
                    "valuationMax", Map.of("type", "integer")
                ),
                "required", List.of("valuationMin", "valuationMax"),
                "additionalProperties", false);
        
        Map<String, Object> request =
                Map.of(
                    "model", "gpt-5-nano",
                    "messages", List.of(
                        Map.of(
                            "role", "system",
                            "content", "You are a highly skilled car valuation expert."
                        ),
                        Map.of(
                            "role", "user",
                            "content", getValuationQuestion(auction)
                        )
                    ),
                    "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                            "name", "carValuation",
                            "strict", true,
                            "schema", schema
                        )
                    )
                );
                
        // src: https://www.baeldung.com/spring-resttemplate-post-json
        ObjectMapper objectMapper = new ObjectMapper();
        String requestString = objectMapper.writeValueAsString(request);

        HttpEntity<String> requestEntity = new HttpEntity<String>(requestString);
                
        // make the post request
        JsonNode response = restTemplate.postForObject(apiUrl, requestEntity, JsonNode.class);
        
        // gather the inforamtion we want from the response
        JsonNode values = objectMapper.readTree(
            response
                .path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asString()
        );

        // prepare return format
        ValuationResult result = new ValuationResult(
            values.path("valuationMin").asInt(),
            values.path("valuationMax").asInt()
        );
        System.out.println(response);
        System.out.println(result.valuationMin());
        System.out.println(result.valuationMax());

        // return result
        return result;
        
    }
}
