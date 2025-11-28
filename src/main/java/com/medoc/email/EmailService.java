package com.medoc.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            Resend resend = new Resend(resendApiKey);

            SendEmailRequest request = SendEmailRequest.builder()
                    .from("MEDOCS FACILE <no-reply@resend.dev>")  // ✔ works without domain
                    .to(to)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);

            System.out.println("Email sent successfully: " + response.getId());

        } catch (Exception e) {
            System.err.println("Error sending email:");
            e.printStackTrace();
        }
    }
}
