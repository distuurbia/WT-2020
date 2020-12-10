package epam.webtech.exceptions;

import javax.servlet.ServletException;

public class InternalException extends ServletException {

    public InternalException(String message) {
        super(message);
    }
}
