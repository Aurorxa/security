package com.github.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-16 22:15:06
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @NonNull
    private ObjectMapper objectMapper;

    /**
     * 判断类或者方法是否使用了 @ResponseBody
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) || returnType.hasMethodAnnotation(ResponseBody.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 防止重复包裹的问题出现
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof String) {
            System.out.println("Result.success(body) = " + Result.success(body));
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.success(body));
        }
        return Result.success(body);
    }

    /**
     * 处理 JSR 303 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Result<?> handleValidatedException(Exception e) {
        Result<String> resp = null;

        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            log.info("handleValidatedException = {}", e.getMessage());
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            resp = Result.error(ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("; ")));
        } else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            log.info("handleValidatedException = {}", e.getMessage());
            ConstraintViolationException ex = (ConstraintViolationException) e;
            resp = Result.error(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; ")));
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            log.info("handleValidatedException = {}", e.getMessage());
            BindException ex = (BindException) e;
            resp = Result.error(ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("; ")));
        }
        return resp;
    }

    @ExceptionHandler(value = {Exception.class})
    public Result<?> handleException(Exception e) {
        log.info("handleException = {}", e.getMessage());
        return Result.error(e.getMessage());
    }

}
