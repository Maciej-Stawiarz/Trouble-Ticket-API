package ms.Trouble_Ticket_API.trouble_ticket.models.dtos;

import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;

public class TroubleTicketSummary {
	
	public String externalID;
	public Integer serviceID;
	public String description;
	public TroubleTicketStatus status;
}
