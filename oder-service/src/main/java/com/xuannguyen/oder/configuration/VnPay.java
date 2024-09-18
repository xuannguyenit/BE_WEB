package com.xuannguyen.oder.configuration;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VnPay {
    // URL để thực hiện thanh toán
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    // Mã thương mại của bạn

    public static String vnp_TmnCode = "J3JO6856";

    // Khóa bí mật dùng để tạo chữ ký bảo mật HMAC-SHA512

    public static String vnp_HashSecret ="1AYE08U3VK3G6DI09WJ0BPR2M9SQB0ZH";
    // Phiên bản API
    public static String vnp_Version = "2.1.0";
    // Lệnh API cho việc thanh toán
    public static String vnp_Command = "pay";



    // URL để gửi yêu cầu API đến VNPay
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    // Phương thức tạo mã hash SHA-256 cho chuỗi đầu vào
    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = ""; // Xử lý lỗi nếu mã hóa không được hỗ trợ
        } catch (NoSuchAlgorithmException ex) {
            digest = ""; // Xử lý lỗi nếu thuật toán không được tìm thấy
        }
        return digest;
    }

    // Phương thức tạo chữ ký HMAC-SHA512 cho tất cả các trường dữ liệu
    public static String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames); // Sắp xếp tên các trường
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
                sb.append("&");
            }
        }
        sb.setLength(sb.length() - 1); // Loại bỏ ký tự '&' cuối cùng
        return hmacSHA512(vnp_HashSecret, sb.toString()); // Tạo chữ ký HMAC-SHA512
    }

    // Phương thức tạo chữ ký HMAC-SHA512 từ khóa bí mật và dữ liệu
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException(); // Kiểm tra giá trị null
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff)); // Chuyển đổi kết quả thành định dạng hex
            }
            return sb.toString();
        } catch (Exception ex) {
            return ""; // Xử lý lỗi
        }
    }

    // Phương thức lấy địa chỉ IP của người dùng từ yêu cầu HTTP
    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR"); // Lấy địa chỉ IP từ header
            if (ipAdress == null) {
                ipAdress = request.getLocalAddr(); // Nếu không có header, lấy địa chỉ IP cục bộ
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage(); // Xử lý lỗi nếu có
        }
        return ipAdress;
    }

    // Phương thức tạo số ngẫu nhiên có độ dài `len`
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length()))); // Chọn ngẫu nhiên ký tự từ chuỗi
        }
        return sb.toString();
    }

    // Phương thức tạo các tham số yêu cầu thanh toán và chữ ký
    public static Map<String, String> createPaymentRequestParams(String orderId, double amount, String returnUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnp_Version);
        params.put("vnp_Command", vnp_Command);
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf((int) (amount * 100))); // Số tiền theo đơn vị đồng
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Order payment");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", "127.0.0.1"); // Thay bằng địa chỉ IP thực nếu cần
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); // Thời gian tạo yêu cầu

        String hash = hashAllFields(params); // Tạo chữ ký bảo mật cho các tham số
        params.put("vnp_SecureHash", hash);

        return params;
    }
}
