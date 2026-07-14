package ms.Trouble_Ticket_API.trouble_ticket.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;

@AllArgsConstructor
public class TroubleTicketSummary {
	
	@NotBlank
	public String externalId;
	
	@NotNull
	@Min(1)
	public Long serviceId;
	
	@NotBlank
	public String description;
	
	public TroubleTicketStatus status;
}
