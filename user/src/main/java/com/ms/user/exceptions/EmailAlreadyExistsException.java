package com.ms.user.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("O Email informado já está cadastrado.");
    }
}
