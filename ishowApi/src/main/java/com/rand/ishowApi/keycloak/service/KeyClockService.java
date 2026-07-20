package com.rand.ishowApi.keycloak.service;



import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.keycloak.request.CreateUserRequest;
import com.rand.ishowApi.keycloak.response.TokenResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeyClockService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    // ================= LOGIN =================
    public TokenResponse login(String username, String password) {

        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("scope", "openid profile email");
        form.add("username", username);
        form.add("password", password);

        try {
            return restTemplate.postForObject(
                    url,
                    new HttpEntity<>(form, formHeaders()),
                    TokenResponse.class
            );
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.AUTH_LOGIN_FAILED, null);
        }
    }


    // ================= ADMIN TOKEN =================
    public TokenResponse getAdminToken() {

        String url = serverUrl + "/realms/master/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", "admin-cli");
        form.add("grant_type", "password");
        form.add("username", adminUsername);
        form.add("password", adminPassword);

        return restTemplate.postForObject(
                url,
                new HttpEntity<>(form, formHeaders()),
                TokenResponse.class
        );
    }

    // ================= REGISTER USER =================




    public String registerUser(
            CreateUserRequest request
    ) {

        String accessToken = getAdminToken().getAccessToken();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("gsm", List.of(String.valueOf(request.getGsm())));
        attributes.put("operator", List.of(String.valueOf(request.getOperator())));

        Map<String, Object> payload = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "firstName", request.getFirstName(),
                "lastName",request.getLastName(),
                "enabled", true,
                "attributes",attributes,
                "emailVerified", true,
                "requiredActions", List.of(),
                "credentials", List.of(
                        Map.of(
                                "type", "password",
                                "value", request.getPassword(),
                                "temporary", false
                        )
                )
        );

        HttpHeaders headers = jsonHeaders(accessToken);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                serverUrl + "/admin/realms/" + realm + "/users",
                new HttpEntity<>(payload, headers),
                Void.class
        );

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null) {
            throw new CustomException("USER_CREATION_FAILED", null);
        }

        String userId = location.substring(location.lastIndexOf("/") + 1);

        assignRealmRole(userId, request.getRole().getCode(), accessToken);

        return userId;
    }

    // ================= ASSIGN ROLE =================
    private void assignRealmRole(String userId, String roleCode, String adminToken) {

        HttpHeaders headers = jsonHeaders(adminToken);

        String roleUrl = serverUrl + "/admin/realms/" + realm + "/roles/" + roleCode;

        Map<?, ?> role = restTemplate.exchange(
                roleUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        ).getBody();

        if (role == null) {
            throw new CustomException("ROLE_NOT_FOUND", null);
        }

        String assignUrl = serverUrl + "/admin/realms/" + realm +
                "/users/" + userId + "/role-mappings/realm";

        restTemplate.postForEntity(
                assignUrl,
                new HttpEntity<>(List.of(role), headers),
                Void.class
        );
    }

    // ================= HEADERS =================
    private HttpHeaders formHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return h;
    }

    private HttpHeaders jsonHeaders(String token) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(token);
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    public TokenResponse refresh(String refreshToken) {

        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("scope", "openid profile email");
        form.add("refresh_token", refreshToken);

        try {
            return restTemplate.postForObject(url,
                    new HttpEntity<>(form, formHeaders()),
                    TokenResponse.class);

        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.AUTH_REFRESH_FAILED, null);
        }
    }

    public void logout(String refreshToken) {

        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);

        try {
            restTemplate.postForEntity(url,
                    new HttpEntity<>(form, formHeaders()),
                    String.class);
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.AUTH_LOGOUT_FAILED, null);
        }
    }

    public void resetPassword(String userId, String newPassword) {
        String accessToken = getAdminToken().getAccessToken();
        HttpHeaders headers = jsonHeaders(accessToken);

        Map<String, Object> payload = Map.of(
                "type", "password",
                "value", newPassword,
                "temporary", false
        );

        String url = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(payload, headers),
                Void.class
        );
    }

    public boolean userExists(String usernameOrEmail) {

        HttpHeaders headers = jsonHeaders(getAdminToken().getAccessToken());
        String url = serverUrl + "/admin/realms/" + realm + "/users?username=" + usernameOrEmail;

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );

        List<?> users = response.getBody();
        return users != null && !users.isEmpty();
    }

    public Map<String, Object> findByUserId(String userId) {
        String accessToken = getAdminToken().getAccessToken();
        HttpHeaders headers = jsonHeaders(accessToken);

        String url = serverUrl + "/admin/realms/" + realm + "/users/" + userId;

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            ).getBody();
        } catch (Exception e) {
            throw new CustomException("USER_NOT_FOUND", null);
        }
    }

    public Map<String, Object> findByEmail(String email) {
        String accessToken = getAdminToken().getAccessToken();
        HttpHeaders headers = jsonHeaders(accessToken);

        String url = serverUrl + "/admin/realms/" + realm + "/users?email=" + email + "&exact=true";

        List<Map<String, Object>> users = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        ).getBody();

        if (users == null || users.isEmpty()) {
            throw new CustomException("USER_NOT_FOUND", null);
        }

        return users.get(0);
    }



    public void deleteKeycloakUser(String userId) {
        String accessToken = getAdminToken().getAccessToken();
        HttpHeaders headers = jsonHeaders(accessToken);

        String url = serverUrl + "/admin/realms/" + realm + "/users/" + userId;

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );
    }


    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject(); // Keycloak userId
        }

        return null; // no user authenticated
    }
}