package com.boromak.interviews.recipebook.common;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;

/**
 * A DTO to map the {@link DefaultErrorAttributes}. The default handler from Spring does not have a
 * DTO and uses a plain map.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponseDto {
  /** Timestamp of when the error occurred. */
  private Date timestamp;

  /** HTTP status code. */
  private Integer status;

  /** Error type (bad request, internal server error, etc). */
  private String error;

  /** Error message. */
  private String message;

  /** Request path that caused the error. */
  private String path;
}
