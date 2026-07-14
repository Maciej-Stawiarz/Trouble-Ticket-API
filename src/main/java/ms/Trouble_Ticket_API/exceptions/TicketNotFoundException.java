package ms.Trouble_Ticket_API.exceptions;

public class TicketNotFoundException extends RuntimeException {
	
	public TicketNotFoundException(String message) {
		super(message);
	}
}
