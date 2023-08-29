package com.adan.springemail.scheduler;

import com.adan.springemail.service.impl.EmailServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {

    private final EmailServiceImpl emailService;

    public EmailScheduler(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 600000) // Run every 10 minutes
    public void pullAndMonitorEmails() {
        emailService.monitorIncomingEmails();
    }
}