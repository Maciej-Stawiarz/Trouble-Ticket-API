package ms.Trouble_Ticket_API.trouble_ticket.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;

public class TroubleTicketSummary {
	
	@NotBlank
	public String externalID;
	
	@NotNull
	@Min(1)
	public Long serviceID;
	
	@NotBlank
	public String description;
	
	public TroubleTicketStatus status;
}
