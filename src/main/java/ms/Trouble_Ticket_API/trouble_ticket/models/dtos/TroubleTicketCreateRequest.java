package ms.Trouble_Ticket_API.trouble_ticket.models.dtos;

import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketCreateStatus;

public class TroubleTicketCreateRequest {
	
	public String externalID;
	public Integer serviceID;
	public String description;
	public TroubleTicketCreateStatus status;
	public String note;
}
