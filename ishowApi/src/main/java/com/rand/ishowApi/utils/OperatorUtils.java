package com.rand.ishowApi.utils;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.shared.operator.Operator;

public class OperatorUtils {

    public static Operator getGsmOperator(String gsm) {
        if (gsm == null || gsm.length() != 10) {
            throw new CustomException("invalid.gsm", null);
        }


        String prefix = gsm.substring(0, 3);


        if (prefix.equals("099") || prefix.equals("098") || prefix.equals("093")) {
            return Operator.SYRIATEL;
        }
        if (prefix.equals("094") || prefix.equals("095") || prefix.equals("096")) {
            return Operator.MTN;
        }
        throw new CustomException("invalid.operator", null);

    }
}