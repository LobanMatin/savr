package com.lobanmating.budget_api.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("The following email is already registered to an account: " + email);
    }
}
