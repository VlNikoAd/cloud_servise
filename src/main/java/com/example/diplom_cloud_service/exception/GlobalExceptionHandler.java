package com.example.diplom_cloud_service.exception;

import com.example.diplom_cloud_service.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<Error> catchInvalidDataException(InvalidDataException e){
		return new ResponseEntity<>(new Error(e.getMessage(),HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler
	public ResponseEntity<Error> catchInvalidJwtException(InvalidJwtException e){
		return new ResponseEntity<>(new Error(e.getMessage(),HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler
	public ResponseEntity<Error> catchUserNotFoundException(UserNotFoundException e){
		return new ResponseEntity<>(new Error(e.getMessage(),HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler
	public ResponseEntity<Error> catchMaxUploadSizeExceeded(MaxUploadSizeExceededException e){
		return new ResponseEntity<>(new Error(e.getMessage(),HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
	}

}
