package ms.Trouble_Ticket_API.trouble_ticket;

import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("troubleTicket")
public interface TroubleTicketAPI {
	
	@PostMapping
	ResponseEntity<TroubleTicket> createTroubleTicket(@RequestBody TroubleTicketCreateRequest request);
	
	@GetMapping
	ResponseEntity<List<TroubleTicket>> listTroubleTicket();
	
	@GetMapping("{id}")
	ResponseEntity<TroubleTicket> getTroubleTicketById();
	
	@PatchMapping("{id}")
	ResponseEntity<TroubleTicket> closeTroubleTicket();
}
