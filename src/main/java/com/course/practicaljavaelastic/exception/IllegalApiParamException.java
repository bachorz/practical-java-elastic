package com.course.practicaljavaelastic.exception;

public class IllegalApiParamException extends RuntimeException {

    private static final long serialVersionUID = 8451094369590154905L;

    public IllegalApiParamException(String message) {
        super(message);
    }
}
