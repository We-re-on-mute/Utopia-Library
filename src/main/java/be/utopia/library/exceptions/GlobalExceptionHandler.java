package be.utopia.library.exceptions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;

import com.google.firebase.auth.FirebaseAuthException;

import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@ComponentScan
public class GlobalExceptionHandler {

    Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Log the exception to the logger
     * @param request
     * @param level
     * @param ex
     */
    public void logRequestException(WebRequest request, Level level, Exception ex){
        String sessionId = request.getSessionId();
        String exceptionString = ex.toString();
        logger.log(level, String.format("Global exception handeled for Session [%1$s] with exception [%2$s]", sessionId, exceptionString));
    }

    /**
     * Handle general exception on internal server error
     * @param request
     * @param ex
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final void handle(WebRequest request, Exception ex) {
        this.logRequestException(request, Level.ERROR, ex);
    }

     /**
      * Handle no such element exceptions on bad request
      * @param request
      * @param ex
      */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
     public final void handle(WebRequest request, NoSuchElementException ex){
          this.logRequestException(request, Level.WARN, ex);
     }

     /**
      * Handle illegal argument exceptions on bad request
      * @param request
      * @param ex
      */
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     @ExceptionHandler(IllegalArgumentException.class)
     public final void handle(WebRequest request, IllegalArgumentException ex){
          this.logRequestException(request, Level.WARN, ex);
     }

     /**
      * Handle entity not found exceptions on bad request
      * @param request
      * @param ex
      */
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     @ExceptionHandler(EntityNotFoundException.class)
     public final void handle(WebRequest request, EntityNotFoundException ex){
          this.logRequestException(request, Level.WARN, ex);
     }

     /**
      * Handle number format exceptions on bad request
      * @param request
      * @param ex
      */
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     @ExceptionHandler(NumberFormatException.class)
     public final void handle(WebRequest request, NumberFormatException ex){
          this.logRequestException(request, Level.WARN, ex);
     }

     /**
      * Handle firebase auth exceptions on unauthorized
      * @param request
      * @param ex
      */
     @ResponseStatus(HttpStatus.UNAUTHORIZED)
     @ExceptionHandler(FirebaseAuthException.class)
     public final void handle(WebRequest request, FirebaseAuthException ex){
          this.logRequestException(request, Level.WARN, ex);
     }

    
}
