package com.xkball.xkdeco.utils.exception;

public class ModelParseException extends IllegalArgumentException {

    public ModelParseException(String s) {
        super(s);
    }

    public ModelParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelParseException(Throwable cause) {
        super(cause);
    }
}
