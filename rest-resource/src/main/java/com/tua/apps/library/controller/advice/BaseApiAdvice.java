package com.tua.apps.library.controller.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.tua.apps.library.exception.ApiException;
import com.tua.apps.library.exception.NotFoundException;
import com.tua.apps.pojo.RestResponsePojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseApiAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    RestResponsePojo<List<ObjectError>> handleMethodArgumentException(MethodArgumentNotValidException e, final Model model, HttpServletResponse response) {
        log.error("Exception during execution of application "+ e.getMessage());
        RestResponsePojo< List<ObjectError> > restPojo = new RestResponsePojo<>();
        log.info(e.getLocalizedMessage());
        BeanPropertyBindingResult beanPropertyBindingResult = (BeanPropertyBindingResult) e.getBindingResult();
        List<ObjectError> objectErrors = beanPropertyBindingResult.getAllErrors();
        String errorMessage = "Unable to process request. Validation error(s): ".concat(objectErrors
                .stream()
                .map(ObjectError::getDefaultMessage)
                .filter(m -> StringUtils.hasText(m) && !m.toLowerCase().contains("javax".toLowerCase()))
                .collect(Collectors.joining("; ")));
        restPojo.setData(objectErrors);
        restPojo.setMessage(errorMessage);
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        restPojo.setStatus(HttpStatus.BAD_REQUEST.value());
        return restPojo;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    RestResponsePojo<?> illegalArgumentException(Exception e, final Model model, HttpServletResponse response) {
        log.error("Exception during execution of application "+ e.getMessage());
        RestResponsePojo restPojo = new RestResponsePojo();
        log.info(e.getLocalizedMessage());
        restPojo.setMessage(e.getMessage());
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        e.printStackTrace();
        restPojo.setStatus(HttpStatus.BAD_REQUEST.value());

        return restPojo;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    RestResponsePojo<Boolean> handleMessageNotReadable(Exception e, final Model model, HttpServletResponse response) {
        log.error("Exception during execution of application "+ e.getMessage());
        RestResponsePojo restPojo = new RestResponsePojo();
        restPojo.setMessage("Http Message Unreadable.");
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        restPojo.setStatus(HttpStatus.BAD_REQUEST.value());

        return restPojo;
    }

    @ExceptionHandler({JsonParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    RestResponsePojo<Boolean> jsonParseException(Exception e, final Model model, HttpServletResponse response) {
        log.error("Exception during execution  application "+ e.getMessage());
        RestResponsePojo restPojo = new RestResponsePojo();
        restPojo.setMessage("Invalid JSON.");
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        restPojo.setStatus(HttpStatus.BAD_REQUEST.value());

        return restPojo;
    }

    @ExceptionHandler({ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    RestResponsePojo<Boolean> apiException(ApiException e, HttpServletResponse response) {
        log.error("Exception during execution of  application " + e.getMessage());
        RestResponsePojo restPojo = getRestResponsePojo(e, response);

        return restPojo;
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    RestResponsePojo<Boolean> notFoundException(NotFoundException e, HttpServletResponse response) {
        log.error("Exception during execution of application " + e.getMessage());

        e.printStackTrace();
        RestResponsePojo restPojo = new RestResponsePojo();
        restPojo.setMessage(e.getMessage());
        restPojo.setStatus(HttpStatus.NOT_FOUND.value());
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        return restPojo;
    }

    private RestResponsePojo getRestResponsePojo(Exception e, HttpServletResponse response) {
        e.printStackTrace();
        RestResponsePojo restPojo = new RestResponsePojo();
        restPojo.setMessage(e.getMessage());
        restPojo.setStatus(HttpStatus.BAD_REQUEST.value());
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        return restPojo;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    RestResponsePojo<Boolean> exception(Exception e, HttpServletResponse response) {
        log.error("Exception during execution of  application " + e.getMessage());
        e.printStackTrace();
        RestResponsePojo restPojo = new RestResponsePojo();
        restPojo.setMessage("Unable to process request at the moment | " + e.getMessage());
        restPojo.setSuccess(Boolean.FALSE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        restPojo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return restPojo;
    }

}
