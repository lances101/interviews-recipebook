package com.boromak.interviews.recipebook.common;

import java.util.Date;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Generic exception handler controller advice. This class is used to handle all exceptions thrown
 * by the application.
 */
@ControllerAdvice
public class RecipeBookExceptionHandler {

  /**
   * Handles {@link EntityNotFoundException}s.
   *
   * @param ex Exception to handle.
   * @param request Web request.
   * @return Response entity with status code 404.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public final ResponseEntity<Object> handleBadRequestExceptions(Exception ex, WebRequest request) {
    ErrorResponseDto errorDto =
        new ErrorResponseDto(
            new Date(),
            HttpStatus.NOT_FOUND.value(),
            "Resource not found",
            ex.getLocalizedMessage(),
            ((ServletWebRequest) request).getRequest().getRequestURI());
    return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles {@link MethodArgumentTypeMismatchException}s.
   *
   * @param ex Exception to handle.
   * @param request Web request.
   * @return Response entity with status code 400.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public final ResponseEntity<Object> handleBadArgumentType(Exception ex, WebRequest request) {
    ErrorResponseDto errorDto =
        new ErrorResponseDto(
            new Date(),
            HttpStatus.BAD_REQUEST.value(),
            "Argument type mismatch",
            ex.getLocalizedMessage(),
            ((ServletWebRequest) request).getRequest().getRequestURI());
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
}
