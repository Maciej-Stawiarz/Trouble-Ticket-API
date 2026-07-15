package ms.Trouble_Ticket_API.exceptions.model.exceptions;

public class TicketNotFoundException extends RuntimeException {
	
	public TicketNotFoundException(String message) {
		super(message);
	}
}
