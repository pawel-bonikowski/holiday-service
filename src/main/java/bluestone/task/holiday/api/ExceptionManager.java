package bluestone.task.holiday.api;

import bluestone.task.holiday.domain.UnsupportedCountryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDate;

@ControllerAdvice
class ExceptionManager {

    private static final Logger log = LoggerFactory.getLogger(ExceptionManager.class);

    @ExceptionHandler(UnsupportedCountryException.class)
    public ResponseEntity<ProblemDetail> unexpectedError(UnsupportedCountryException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ProblemDetail> typeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message;
        if (LocalDate.class.equals(ex.getRequiredType())) {
            message = "Invalid date format. Expected format YYYY-MM-DD";
        } else {
            message = ex.getMessage();
        }
        ProblemDetail body = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        body.setDetail(message);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> resourceNotFound(NoResourceFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> missingRequiredArgument(MissingServletRequestParameterException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Missing required param: %s".formatted(ex.getParameterName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> unexpectedError(Exception ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("Unexpected error");
        log.error("Unexpected Error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
