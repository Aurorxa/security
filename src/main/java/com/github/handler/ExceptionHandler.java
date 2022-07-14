package com.github.handler;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 10:42:43
 */
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {

    @Override
    public boolean isCausalChainsEnabled() {
        // 生产环境下需要关闭，即返回 false
        return true;
    }

}
