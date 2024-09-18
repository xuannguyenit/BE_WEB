package com.xuannguyen.oder.controller;
import com.xuannguyen.oder.configuration.VnPay;

import com.xuannguyen.oder.dto.request.PaymentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @GetMapping("/create_payment/{total_price}")
    public ResponseEntity<?>createPayment(@PathVariable(name="total_price") long total_price) throws UnsupportedEncodingException {


        long price =total_price*100;

        String vnp_TxnRef = VnPay.getRandomNumber(8);

        String vnp_TmnCode = VnPay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPay.vnp_Version);
        vnp_Params.put("vnp_Command", VnPay.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(price));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", "billpayment");  // Set the correct order type
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:4200/payment-success");  // Replace with your actual return URL
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");  // Replace with the actual IP address if necessary



        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append('&');
                hashData.append('&');
            }
        }
        query.setLength(query.length() - 1);  // Remove the last '&'
        hashData.setLength(hashData.length() - 1);  // Remove the last '&'

        String vnp_SecureHash = VnPay.hmacSHA512(VnPay.vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = VnPay.vnp_PayUrl + "?" + query.toString();

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setStatus("Ok");
        paymentRequest.setMessage("Successfully");
        paymentRequest.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentRequest);
    }
    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "payment-success";  // This will return the payment-success.html page
    }
}
