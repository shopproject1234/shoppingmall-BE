package com.sangwook.shoppingmall.exception;

import com.sangwook.shoppingmall.exception.custom.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * ExceptionHandler 추가 방법
     * 각각의 커스텀한 런타임예외에는 method라는 필드가 필요 -> 문제가 발생한 부분의 메서드를 ErrorResponse에 같이 출력하기 위함
     *
     * @ExceptionHandler(XXX.class) public ResponseEntity<ErrorResponse> handleXXXException(XXXException e) {
     * String method = e.getMethod();
     * ErrorResponse error = ErrorResponse.error(e, method);
     * return ResponseEntity.status(xxx).body(error);
     * }
     */

    @ExceptionHandler(MyItemException.class)
    public ResponseEntity<ErrorResponse> handleMyItemException(MyItemException e) {
        String method = e.getMethod();
        ErrorResponse error = ErrorResponse.error(e, method);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendException(EmailSendException e) {
        String method = e.getMethod();
        ErrorResponse error = ErrorResponse.error(e, method);
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSessionNotFoundException(SessionNotFoundException e) {
        String method = e.getMethod();
        ErrorResponse error = ErrorResponse.error(e, method);
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFoundException(ObjectNotFoundException e) {
        String method = e.getMethod();
        ErrorResponse error = ErrorResponse.error(e, method);
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ErrorResponse> handleUserValidationException(UserValidationException e) {
        String method = e.getMethod();
        ErrorResponse error = ErrorResponse.error(e, method);
        return ResponseEntity.status(400).body(error);
    }

}
