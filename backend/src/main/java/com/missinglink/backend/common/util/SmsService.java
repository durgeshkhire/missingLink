package com.missinglink.backend.common.util;

public interface SmsService {

    int SendUVOTP(String mobileNo, String msg, String temId);
}
