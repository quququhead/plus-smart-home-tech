package ru.yandex.practicum.sht.commerce.ia.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    Throwable cause;
    List<StackTraceElement> stackTrace;
    HttpStatus httpStatus;
    String userMessage;
    String message;
    List<Throwable> suppressed;
    String localizedMessage;
}