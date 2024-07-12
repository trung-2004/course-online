package com.example.payment1_service.configuration;

import com.example.payment1_service.util.VNPayUtil;
import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
public class VNPAYConfig {
    @Getter
    //@Value("${payment.snPay.url}")
    private String vnp_PayUrl;
    //@Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    //@Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode ;
    @Getter
    //@Value("${payment.vnPay.secretKey}")
    private String secretKey;
    //@Value("${payment.vnPay.version}")
    private String vnp_Version;
    //@Value("${payment.vnPay.command}")
    private String vnp_Command;
    //@Value("${payment.vnPay.orderType}")
    private String orderType;

    public VNPAYConfig() {
        // Initialize with hardcoded values if needed
        vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        vnp_TmnCode = "RNT2VBPW";
        secretKey = "GAX12WBRHQQN85YG68LBKYCOMPTCD96L";
        vnp_ReturnUrl = "http://localhost:8888/api/v1/payment1/any/return-vnpay";
        vnp_Version = "2.1.0";
        vnp_Command = "pay";
        orderType = "other";
    }

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef",  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" +  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnpCreateDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
//        calendar.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }
}
