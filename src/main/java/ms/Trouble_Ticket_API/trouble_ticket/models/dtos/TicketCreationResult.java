package ms.Trouble_Ticket_API.trouble_ticket.models.dtos;

import lombok.AllArgsConstructor;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;

@AllArgsConstructor
public class TicketCreationResult {
	
	public TroubleTicket troubleTicket;
	public boolean wasCreated;
}
