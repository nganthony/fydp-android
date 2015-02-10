package com.uwaterloo.fydp.api;

/**
 * Custom exception class with HTTP status code and error message
 * @author Anthony
 *
 */
public class ApplicationException extends Exception {
    int statusCode = -1;
    String errorMessage = "";

    public ApplicationException(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public ApplicationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
