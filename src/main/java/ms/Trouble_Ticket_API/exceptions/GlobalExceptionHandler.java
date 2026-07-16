package ms.Trouble_Ticket_API.exceptions;

import ms.Trouble_Ticket_API.exceptions.model.dtos.Error;
import ms.Trouble_Ticket_API.exceptions.model.enums.ErrorCode;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.ServiceNotFoundException;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.TicketNotFoundException;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.ValidationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ms.Trouble_Ticket_API.security.RequestIdFilter.MDC_KEY;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ValidationException.class)
	ResponseEntity<ms.Trouble_Ticket_API.exceptions.model.dtos.Error> handleValidationException(ValidationException exception) {
		ms.Trouble_Ticket_API.exceptions.model.dtos.Error error = ms.Trouble_Ticket_API.exceptions.model.dtos.Error.builder()
				.code(ErrorCode.VALIDATION_ERROR.name())
				.message(exception.getMessage())
				.requestID(MDC.get(MDC_KEY))
				.build();
		
		return new ResponseEntity<>(
				error,
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TicketNotFoundException.class)
	ResponseEntity<ms.Trouble_Ticket_API.exceptions.model.dtos.Error> handleTicketNotFoundException(TicketNotFoundException exception) {
		ms.Trouble_Ticket_API.exceptions.model.dtos.Error error = Error.builder()
				.code(ErrorCode.TROUBLE_TICKET_NOT_FOUND.name())
				.message(exception.getMessage())
				.requestID(MDC.get(MDC_KEY))
				.build();
		
		return new ResponseEntity<>(
				error,
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ServiceNotFoundException.class)
	ResponseEntity<Error> handleServiceNotFoundException(ServiceNotFoundException exception) {
		Error error = Error.builder()
				.code(ErrorCode.SERVICE_NOT_FOUND.name())
				.message(exception.getMessage())
				.requestID(MDC.get(MDC_KEY))
				.build();
		
		return new ResponseEntity<>(
				error,
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Error> handleMethodNotValidException(MethodArgumentNotValidException exception) {
		Error error = Error.builder()
				.code(ErrorCode.VALIDATION_ERROR.name())
				.message(exception.getMessage())
				.requestID(MDC.get(MDC_KEY))
				.build();
		
		return new ResponseEntity<>(
				error,
				HttpStatus.BAD_REQUEST);
	}
}