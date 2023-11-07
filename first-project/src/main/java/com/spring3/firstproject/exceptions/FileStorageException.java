package com.spring3.firstproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileStorageException(String err) {
        super(err);
    }

    public FileStorageException(String err, Throwable cause) {
        super(err, cause);
    }
}
