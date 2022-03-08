package com.extract.exception;

public class DirectoryNotFoundException extends RuntimeException {

    public DirectoryNotFoundException() {

    }

    public DirectoryNotFoundException(String message) {
        super(message);
    }

}
