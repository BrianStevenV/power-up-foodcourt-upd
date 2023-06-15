package com.example.foodcourtmicroservice.domain.model.Order;

public class EmailUser {
    private String emailClient;
    private String emailEmployee;

    public EmailUser(String emailClient, String emailEmployee) {
        this.emailClient = emailClient;
        this.emailEmployee = emailEmployee;
    }

    public EmailUser() {

    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getEmailEmployee() {
        return emailEmployee;
    }

    public void setEmailEmployee(String emailEmployee) {
        this.emailEmployee = emailEmployee;
    }
}
