package ms.Trouble_Ticket_API.exceptions.model.exceptions;

public class ValidationException extends RuntimeException {
	
	public ValidationException(String message) {
		super(message);
	}
}
