package com.corebankingsystem.CustomerMs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean hasActiveAccounts(Long customerId) {
        String url = "http://localhost:8087/accounts/customer/" + customerId;
        try {
            List<?> response = restTemplate.exchange(url, HttpMethod.GET, null, List.class).getBody();
            return response != null && !response.isEmpty();
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}
