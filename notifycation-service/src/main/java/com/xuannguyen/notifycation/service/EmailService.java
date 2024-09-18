package com.xuannguyen.notifycation.service;

import com.xuannguyen.notifycation.dto.request.EmailRequest;
import com.xuannguyen.notifycation.dto.request.SendEmailRequest;
import com.xuannguyen.notifycation.dto.request.Sender;
import com.xuannguyen.notifycation.dto.response.EmailResponse;
import com.xuannguyen.notifycation.exception.AppException;
import com.xuannguyen.notifycation.exception.ErrorCode;
import com.xuannguyen.notifycation.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()// người gửi
                        .name("xuannam")
                        .email("xuanthuyloi@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())// nội dung thông báo
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
