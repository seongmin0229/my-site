package com.bitacademy.mysite.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public String handlerException(Exception e) {
		// 1. 로깅
		e.printStackTrace();
		
		// 2. 사과
		return "error/exception";
	}
}
