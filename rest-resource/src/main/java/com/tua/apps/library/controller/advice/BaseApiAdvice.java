package com.tua.apps.library.controller.advice;

import com.tua.apps.library.exception.ApiException;
import com.tua.apps.pojo.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseApiAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value= HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public GenericResponse requestHandlingNoHandlerFound(NoHandlerFoundException e, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return GenericResponse.builder()
                .error(String.format("Requested resource not found. METHOD: %s. PATH: %s", e.getHttpMethod(), e.getRequestURL()))
                .build();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    GenericResponse handleConstraintViolationException(ConstraintViolationException e, final Model model, HttpServletResponse response) {
        log.error("Exception during execution of application "+ e.getMessage());

        Set<ConstraintViolation<?>> errors  =  e.getConstraintViolations();

        String errorMessage = "Validation error(s): ".concat(errors
                .stream()
                .map(ConstraintViolation::getMessage)
                .filter(m -> StringUtils.hasText(m) && !m.toLowerCase().contains("javax".toLowerCase()))
                .collect(Collectors.joining("; ")));

        return GenericResponse.builder().error(errorMessage).build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    GenericResponse handleMethodArgumentException(MethodArgumentNotValidException e, final Model model, HttpServletResponse response) {
        log.error("Exception during execution of application "+ e.getMessage());
        log.info(e.getLocalizedMessage());
        BeanPropertyBindingResult beanPropertyBindingResult = (BeanPropertyBindingResult) e.getBindingResult();
        List<ObjectError> objectErrors = beanPropertyBindingResult.getAllErrors();
        String errorMessage = "Validation error(s): ".concat(objectErrors
                .stream()
                .map(ObjectError::getDefaultMessage)
                .filter(m -> StringUtils.hasText(m) && !m.toLowerCase().contains("javax".toLowerCase()))
                .collect(Collectors.joining("; ")));

        return GenericResponse.builder().error(errorMessage).build();
    }

    @ExceptionHandler({ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    GenericResponse handleApiException(ApiException e, HttpServletResponse response) {
        log.error("Exception during execution of  application " + e.getMessage());

        return GenericResponse.builder().error(e.getMessage()).build();
    }



    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    GenericResponse handleException(Exception e, HttpServletResponse response) {
        log.error("Exception during execution of  application " + e.getMessage());
        e.printStackTrace();

        return GenericResponse.builder().error("unknown failure").build();
    }

}
