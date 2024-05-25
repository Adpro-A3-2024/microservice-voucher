package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.DTO.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public Mono<UserDTO> getUserById(String userId) {
        return this.webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDTO.class);
    }
}

