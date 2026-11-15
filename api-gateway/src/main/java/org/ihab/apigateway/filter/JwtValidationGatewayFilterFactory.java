package org.ihab.apigateway.filter;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder, @Value("${auth.service.url}") String authServiceUrl) {
        webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }


    @Override
    public GatewayFilter apply(@NonNull Object config) {

        // exchange is the object that pass to use through gateway filter that hold all values about the request and response
//        chain hold the filter chain that we have right now we only have 1
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // http://auth-service:4005
            // specify that the method is get
            // this how to make rest call with java
            return webClient.get()
//                    /validate add this
                    .uri("/validate")
                    // add the token header
                    .header(HttpHeaders.AUTHORIZATION, token)
                    // take the response
                    .retrieve()
                    // there is no body
                    .toBodilessEntity()
                    // then move to the next filter if the code is 200 or success code
                    .then(chain.filter(exchange));
        };

    }
}