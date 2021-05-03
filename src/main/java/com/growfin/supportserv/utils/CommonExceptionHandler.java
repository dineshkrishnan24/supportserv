package com.growfin.supportserv.utils;

import com.growfin.supportserv.constants.vo.ResponseErrorVO;
import com.growfin.supportserv.exceptions.ResourceNotFoundException;
import com.growfin.supportserv.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ResponseErrorVO(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ResponseErrorVO(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ResponseErrorVO(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ResponseErrorVO(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ResponseErrorVO responseErrorVO = new ResponseErrorVO(HttpStatus.BAD_REQUEST);
        responseErrorVO.setMessage("Validation error");
        responseErrorVO.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return buildResponseEntity(responseErrorVO);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        ResponseErrorVO responseErrorVO = new ResponseErrorVO(HttpStatus.BAD_REQUEST);
        responseErrorVO.setMessage("Validation error");
        responseErrorVO.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(responseErrorVO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        ResponseErrorVO responseErrorVO = new ResponseErrorVO(HttpStatus.NOT_FOUND);
        responseErrorVO.setMessage(ex.getMessage());
        return buildResponseEntity(responseErrorVO);
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Object> handleService(ServiceException ex) {
        ResponseErrorVO responseErrorVO = new ResponseErrorVO(ex.getHttpStatus(),ex.getMessage(),ex);
        responseErrorVO.setMessage(ex.getMessage());
        return buildResponseEntity(responseErrorVO);
    }

    private ResponseEntity<Object> buildResponseEntity(ResponseErrorVO responseErrorVO) {
        return new ResponseEntity<>(responseErrorVO, responseErrorVO.getStatus());
    }

}

