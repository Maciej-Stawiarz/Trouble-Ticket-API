package ms.Trouble_Ticket_API.exceptions.model.exceptions;

public class ServiceNotFoundException extends RuntimeException {
	
	public ServiceNotFoundException(String message) {
		super(message);
	}
}
