package com.rand.ishowApi.account.mobile.api.request;

public record AccountChangePasswordRequest(String gsm ,String oldPassword,String password) {
}
