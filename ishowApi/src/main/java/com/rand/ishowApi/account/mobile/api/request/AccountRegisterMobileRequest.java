package com.rand.ishowApi.account.mobile.api.request;

import com.rand.ishowApi.shared.gender.Gender;
import java.time.LocalDate;

public record AccountRegisterMobileRequest(String gsm, String password, String fullName, Gender gender, LocalDate birthdate, String fcmToken) {
}
