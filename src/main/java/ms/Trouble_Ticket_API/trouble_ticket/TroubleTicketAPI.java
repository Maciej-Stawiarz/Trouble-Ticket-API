package ms.Trouble_Ticket_API.trouble_ticket;

import jakarta.validation.Valid;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCloseStatusRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketSummary;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("troubleTicket")
public interface TroubleTicketAPI {
	
	@PostMapping
	ResponseEntity<TroubleTicket> createTroubleTicket(@Valid @RequestBody TroubleTicketCreateRequest request);
	
	@GetMapping
	ResponseEntity<List<TroubleTicketSummary>> listTroubleTicket();
	
	@GetMapping("{id}")
	ResponseEntity<TroubleTicket> getTroubleTicketById(@PathVariable("id") String id);
	
	@PatchMapping("{id}")
	ResponseEntity<TroubleTicket> closeTroubleTicket(
			@PathVariable("id") String id,
			@Valid @RequestBody TroubleTicketCloseStatusRequest request);
}
