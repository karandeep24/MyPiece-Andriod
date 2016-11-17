package com.mi.metadata;

import java.util.Vector;

/**
 * Created by karandsingh on 16-08-31.
 */
public class LoginResponseMetadata {

    String status;
    String message;
    String message_fr;
    String userId;
    Vector<CreditCardMetadata> creditCardMetadata;
    String email;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Vector<CreditCardMetadata> getCreditCardMetadata() {
        return creditCardMetadata;
    }

    public void setCreditCardMetadata(Vector<CreditCardMetadata> creditCardMetadata) {
        this.creditCardMetadata = creditCardMetadata;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage_fr() {
        return message_fr;
    }

    public void setMessage_fr(String message_fr) {
        this.message_fr = message_fr;
    }
}
