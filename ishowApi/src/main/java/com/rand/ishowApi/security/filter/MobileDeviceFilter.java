package com.rand.ishowApi.security.filter;

import com.rand.ishowApi.appVersion.application.service.AppVersionMobileService;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.security.context.DeviceInfo;
import com.rand.ishowApi.shared.language.Language;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class MobileDeviceFilter extends OncePerRequestFilter {
    private final AppVersionMobileService appVersionMobileService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            if (isMobileRequest(request)) {
                appVersionMobileService.check(request.getHeader("X-Platform"), request.getHeader("X-App-Version"));
                DeviceInfo deviceInfo = DeviceInfo.builder()
                        .appVersion(request.getHeader("X-App-Version"))
                        .mobileModel(request.getHeader("X-Mobile-Model"))
                        .mobileManufacturer(request.getHeader("X-Mobile-Manufacturer"))
                        .platform(request.getHeader("X-Platform"))
                        .osVersion(request.getHeader("X-OS-Version"))
                        .deviceId(request.getHeader("X-Device-Id"))
                        .language(resolveLanguage(request))
                        .build();

                DeviceContext.set(deviceInfo);

            }


            filterChain.doFilter(request, response);

        } finally {
            DeviceContext.clear();
        }
    }

    private boolean isMobileRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/mobile/");
    }




    private Language resolveLanguage(HttpServletRequest request) {

        String acceptLanguage = request.getHeader("Accept-Language");

        if (acceptLanguage == null || acceptLanguage.isBlank()) {
            return Language.EN;
        }

        // Examples: "en", "en-US", "ar", "ar-EG"
        if (acceptLanguage.toLowerCase().startsWith("ar")) {
            return Language.AR;
        }

        return Language.EN;
    }
}
