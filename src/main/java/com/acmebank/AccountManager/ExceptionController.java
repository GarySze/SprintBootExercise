package com.acmebank.AccountManager;

import com.acmebank.AccountManager.exception.ClientException;
import com.acmebank.AccountManager.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = ServerException.class)
    public ResponseEntity<Object> exception(ServerException exception) {
        return new ResponseEntity<>("Server side exception: " +exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ClientException.class)
    public ResponseEntity<Object> exception(ClientException exception) {
        return new ResponseEntity<>("Bad request exception: " +exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
