package com.kk.marketing.web.conf;

import com.kk.arch.common.exception.BusinessException;
import com.kk.arch.common.vo.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.kk.arch.common.util.AssertUtils.ERROR_CODE;

/**
 * @author Zal
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求参数校验错误处理.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseData<Object>> handleBusinessException(BusinessException e) {
        ResponseData<Object> errorResponse = ResponseData.builder().code(e.getCode()).msg(e.getMsg()).build();
        return new ResponseEntity<ResponseData<Object>>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Object>> handleGenericException(Exception e) {
        ResponseData<Object> errorResponse = ResponseData.builder().code(ERROR_CODE).msg(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

}