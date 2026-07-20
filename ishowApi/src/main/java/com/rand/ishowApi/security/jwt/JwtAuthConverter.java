package com.rand.ishowApi.security.jwt;


import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken>  {

    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();


    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Value("${jwt.auth.converter.principal-attribute}")
    private String principleAttribute;

    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public AbstractAuthenticationToken convert(@Nonnull Jwt jwt) {

        Collection<GrantedAuthority> authorities =
                Stream.concat(
                        grantedAuthoritiesConverter.convert(jwt).stream(),
                        extractResourceRoles(jwt).stream()
                ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipalClaimName(jwt)
        );
    }

    private String getPrincipalClaimName(Jwt jwt) {
        Object value = jwt.getClaim(principleAttribute);
        return value != null ? String.valueOf(value) : jwt.getClaim(JwtClaimNames.SUB);
    }
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
        Collection<String> roles = (Collection<String>) resourceAccess.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
    public Map<String, Object> extractCustomClaims(Jwt jwt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", jwt.getClaim("sid"));
        claims.put("gsm", jwt.getClaim("gsm"));
        claims.put("email_verified", jwt.getClaim("email_verified"));
        claims.put("preferred_username", jwt.getClaim("preferred_username"));
        claims.put("operator", jwt.getClaim("operator"));
        claims.put("email", jwt.getClaim("email"));
        claims.put("sub", jwt.getClaim("sub"));
        claims.put("account_id", jwt.getClaim("account_id"));
        return claims;
    }
    public Set<String> extractRolesFromToken(String accessToken) {
        try {
            String[] parts = accessToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> body = mapper.readValue(payload, Map.class);

            Map<String, Object> realmAccess = (Map<String, Object>) body.get("realm_access");
            if (realmAccess == null) return Collections.emptySet();

            List<String> roles = (List<String>) realmAccess.get("roles");
            return roles != null ? new HashSet<>(roles) : Collections.emptySet();

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract roles from token", e);
        }
    }
}
