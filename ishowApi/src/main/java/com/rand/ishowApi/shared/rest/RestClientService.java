package com.rand.ishowApi.shared.rest;


import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@AllArgsConstructor
public class RestClientService {
    private final RestTemplate restTemplate;

    // POST
    public <T, R> ResponseEntity<R> post(String url, T body, Map<String, String> headers, Class<R> clazz) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            if (headers != null) {
                headers.forEach(httpHeaders::set);
            }
            HttpEntity<T> entity = new HttpEntity<>(body, httpHeaders);
            return restTemplate.exchange(url, HttpMethod.POST, entity, clazz);
        } catch (Exception ex) {
            // Return 500 with exception message as body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((R) ("Internal server error: " + ex.getMessage()));
        }
    }

    // GET
    public <R> ResponseEntity<R> get(String url, Map<String, String> headers, Class<R> clazz) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            if (headers != null) {
                headers.forEach(httpHeaders::set);
            }
            HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
            return restTemplate.exchange(url, HttpMethod.GET, entity, clazz);
        } catch (Exception ex) {
            // Return 500 with exception message as body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((R) ("Internal server error: " + ex.getMessage()));
        }
    }

}
