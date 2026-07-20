package com.rand.ishowApi.account.mobile.api.request;

import com.rand.ishowApi.shared.gender.Gender;
import java.time.LocalDate;

public record AccountEditProfileRequest(String fullName, Gender gender, LocalDate birthdate) {
}
