package com.missinglink.backend.common.util;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

@Service
public class SmsServiceImpl implements SmsService{

    private final String api_key = "NTtgddRKTUOKg7dWw21mrw";     // replace with your real key

    @Override
    public int SendUVOTP(String mobileNo, String msg, String temId) {
        int flag = -1;
        try {
            String url = "http://sendsmsbox.com/api/mt/SendSMS?APIKey=" + api_key
                    + "&senderid=UVKLPA&channel=Trans&DCS=0&flashsms=0"
                    + "&number=" + mobileNo
                    + "&text=" + URLEncoder.encode(msg, "UTF-8")
                    + "&route=2&Peid=1701164371194666524&DLTTemplateId=" + temId;

            org.jsoup.nodes.Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .get();

            JSONObject jsonResponse = new JSONObject(doc.body().text());
            String errorCode = jsonResponse.getString("ErrorCode");

            System.out.println("SMS API response --> " + doc.body().text());

            if (errorCode.equals("000")) {
                System.out.println("OTP SMS sent successfully");
                flag = 1;
            } else {
                System.out.println("OTP SMS failed");
                flag = -1;
            }
        } catch (Exception ex) {
            System.err.println("SMS exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        return flag;
    }


}
